/*******************************************************************************
 * SPDX-License-Identifier: MPL-2.0
 *
 * Copyright (c) 2026 Karlsruhe Institute of Technology (KIT)
 * KASTEL - Dependability of Software-intensive Systems
 *
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package edu.kit.dopler.solvers.smt;

import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import org.sosy_lab.java_smt.SolverContextFactory;

public final class NativeLibLoader {

    // Must  match the <native.libs.dir> property in pom.xml
    private static final String NATIVE_DIR_NAME = "dependencies";

    private static @Nullable Path nativePath = null;
    private static @Nullable Path overrideBasePath = null;
    private static final Map<String, Path> libraryOverrides = new HashMap<>();
    private static boolean warnedUnknownPlatform = false;

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
                nativePath = Path.of(NATIVE_DIR_NAME, getPlatformDirName())
                        .toAbsolutePath()
                        .normalize();
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
        String platformDir = getPlatformDirName();

        // Warning for unknown architectures
        if (platformDir.contains("unknown") && !warnedUnknownPlatform) {
            System.err.println("[WARN] JavaSMT: Unknown OS or Architecture detected (" + System.getProperty("os.name")
                    + " / " + System.getProperty("os.arch") + ").");
            System.err.println("[WARN] Please place your custom native solver binaries directly into the ./"
                    + NATIVE_DIR_NAME + "/ folder.");
            warnedUnknownPlatform = true;
        }

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

        // Local Development Mode (Specific Platform Folder)
        Path localPlatformPath = getNativeLibraryPath().resolve(osLibName).toAbsolutePath();
        if (Files.exists(localPlatformPath)) {
            return Optional.of(localPlatformPath);
        }

        // Manual User Override Fallback (Root dependencies folder)
        // Handles unknown architectures or users forcing a specific binary.
        Path manualOverridePath = Path.of(NATIVE_DIR_NAME, osLibName).toAbsolutePath();
        if (Files.exists(manualOverridePath)) {
            return Optional.of(manualOverridePath);
        }

        // Production Fat JAR Mode (Classpath Extraction)
        String resourcePath = "/" + NATIVE_DIR_NAME + "/" + platformDir + "/" + osLibName;

        try (InputStream is = NativeLibLoader.class.getResourceAsStream(resourcePath)) {
            if (is != null) {
                Path tempDir = Path.of(System.getProperty("java.io.tmpdir"), "smt_natives_" + platformDir);
                Files.createDirectories(tempDir);
                Path targetFile = tempDir.resolve(osLibName);

                // Check if file is missing to avoid Windows AccessDeniedException on locked DLLs
                if (!Files.exists(targetFile)) {
                    try {
                        Files.copy(is, targetFile, StandardCopyOption.REPLACE_EXISTING);
                        targetFile.toFile().deleteOnExit();
                    } catch (IOException e) {
                        // If the file now exists, we can safely ignore the error.
                        if (!Files.exists(targetFile)) {
                            throw e;
                        }
                    }
                }
                return Optional.of(targetFile);
            }
        } catch (IOException e) {
            System.err.println(
                    "Failed extracting native dependency " + osLibName + " from classpath: " + e.getMessage());
        }

        return Optional.empty();
    }

    private static String getPlatformDirName() {
        String os = Ascii.toLowerCase(System.getProperty("os.name"));
        String arch = Ascii.toLowerCase(System.getProperty("os.arch"));

        String osFamily;
        if (os.contains("win")) {
            osFamily = "windows";
        } else if (os.contains("mac") || os.contains("darwin")) {
            osFamily = "macos";
        } else if (os.contains("nux") || os.contains("nix")) {
            osFamily = "linux";
        } else {
            osFamily = "unknown";
        }

        String archNormalized;
        if (arch.contains("amd64") || arch.contains("x86_64")) {
            archNormalized = "x64";
        } else if (arch.contains("aarch64") || arch.contains("arm64")) {
            archNormalized = "arm64";
        } else {
            archNormalized = "unknown";
        }

        return osFamily + "-" + archNormalized;
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
