/*******************************************************************************
 * SPDX-License-Identifier: MPL-2.0
 *
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Copyright 2026 Karlsruhe Institute of Technology (KIT)
 * KASTEL - Dependability of Software-intensive Systems
 *******************************************************************************/
package edu.kit.dopler.solvers.smt;

import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import org.sosy_lab.common.Classes;
import org.sosy_lab.common.NativeLibraries;
import org.sosy_lab.java_smt.SolverContextFactory;

public final class NativeLibLoader {

    private static @Nullable Path nativePath = null;
    private static @Nullable Path overrideBasePath = null;
    private static final Map<String, Path> libraryOverrides = new HashMap<>();

    private NativeLibLoader() {}

    public static void setBasePath(Path basePath) {
        overrideBasePath = basePath;
        nativePath = null;
    }

    public static void setLibraryPath(String libraryName, Path path) {
        libraryOverrides.put(libraryName, path);
    }

    public static void clearOverrides() {
        overrideBasePath = null;
        libraryOverrides.clear();
        nativePath = null;
    }

    public static Path getNativeLibraryPath() {
        if (nativePath == null) {
            if (overrideBasePath != null) {
                nativePath = overrideBasePath.toAbsolutePath().normalize();
            } else {
                String arch = Ascii.toLowerCase(
                        NativeLibraries.Architecture.guessVmArchitecture().name());
                String os = Ascii.toLowerCase(
                        NativeLibraries.OS.guessOperatingSystem().name());

                nativePath = Classes.getCodeLocation(NativeLibraries.class)
                        .getParent()
                        .getParent()
                        .getParent()
                        .resolve(Path.of("native", arch + "-" + os));
            }
        }

        return nativePath;
    }

    public static void loadLibrary(String name) {
        Optional<Path> path = findPathForLibrary(name);

        if (path.isPresent()) {
            System.load(path.get().toAbsolutePath().toString());
        } else {
            System.loadLibrary(name);
        }
    }

    public static Optional<Path> findPathForLibrary(String libraryName) {
        String osLibName = System.mapLibraryName(libraryName);

        // Per-library override
        Path override = libraryOverrides.get(libraryName);
        if (override != null) {
            Path direct = override.resolve(osLibName).toAbsolutePath();
            if (Files.exists(direct)) {
                return Optional.of(direct);
            }
        }

        // Base override path
        if (overrideBasePath != null) {
            Path p = overrideBasePath.resolve(osLibName).toAbsolutePath();
            if (Files.exists(p)) {
                return Optional.of(p);
            }
        }

        // Default behavior
        Path p = getNativeLibraryPath().resolve(osLibName).toAbsolutePath();
        if (Files.exists(p)) {
            return Optional.of(p);
        }

        // Fallback to jar
        p = Classes.getCodeLocation(NativeLibraries.class)
                .resolveSibling(osLibName)
                .toAbsolutePath();

        return Files.exists(p) ? Optional.of(p) : Optional.empty();
    }

    public static void load(SolverContextFactory.Solvers... solvers) {
        for (SolverContextFactory.Solvers solver : solvers) {
            switch (solver) {
                case Z3 -> {
                    loadLibrariesWithFallback(
                            NativeLibLoader::loadLibrary,
                            ImmutableList.of("z3", "z3java"),
                            ImmutableList.of("libz3", "libz3java"));
                    System.setProperty("z3.skipLibraryLoad", "true");
                }
                case CVC5 -> {
                    loadLibrariesWithFallback(
                            NativeLibLoader::loadLibrary, ImmutableList.of("cvc5jni"), ImmutableList.of("libcvc5jni"));
                    System.setProperty("cvc5.skipLibraryLoad", "true");
                }
                default -> throw new IllegalArgumentException("Unsupported solver: " + solver);
            }
        }
    }

    static void loadLibrariesWithFallback(
            Consumer<String> loader, List<String> librariesForFirstTry, List<String> librariesForSecondTry)
            throws UnsatisfiedLinkError {
        Preconditions.checkNotNull(librariesForFirstTry);
        Preconditions.checkNotNull(librariesForSecondTry);
        try {
            librariesForFirstTry.forEach(loader);
        } catch (UnsatisfiedLinkError e1) {
            try {
                librariesForSecondTry.forEach(loader);
            } catch (UnsatisfiedLinkError e2) {
                e1.addSuppressed(e2);
                throw e1;
            }
        }
    }
}
