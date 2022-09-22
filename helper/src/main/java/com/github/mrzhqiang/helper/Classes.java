package com.github.mrzhqiang.helper;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Java 的类工具。
 */
public final class Classes {
    private Classes() {
        // no instance
    }

    /** URL protocol for a file in the file system: "file". */
    public static final String URL_PROTOCOL_FILE = "file";

    /**
     * 从类名称生成类实例。
     *
     * @param className 类的全限定名称，即包括完整包名的类路径。
     * @param <T>       指定类型，通过泛型消除强制转换，有可能导致转换异常，使用者需要注意类型是否一致。
     * @return 生成的类实例。注意，可能返回 Null 值。
     */
    @Nullable
    public static <T> T ofInstance(String className) {
        return ofInstance(className, null);
    }

    /**
     * 从类名称生成类实例。
     *
     * @param className       类的全限定名称，即包括完整包名的类路径。
     * @param defaultInstance 默认实例。如果类名称为 Null 或空串，以及生成实例失败，则返回此默认实例。
     * @param <T>             指定类型，通过泛型消除强制转换，有可能导致转换异常，使用者需要注意类型是否一致。
     * @return 生成的类实例。此方法是否返回 Null 值由默认值决定。
     */
    @SuppressWarnings("unchecked")
    public static <T> T ofInstance(String className, T defaultInstance) {
        if (Strings.isNullOrEmpty(className)) {
            return defaultInstance;
        }

        try {
            return (T) Class.forName(className).newInstance();
        } catch (Exception ignored) {
        }
        return defaultInstance;
    }

    /**
     * Return the default ClassLoader to use: typically the thread context
     * ClassLoader, if available; the ClassLoader that loaded the ClassUtils
     * class will be used as fallback.
     * <p>Call this method if you intend to use the thread context ClassLoader
     * in a scenario where you clearly prefer a non-null ClassLoader reference:
     * for example, for class path resource loading (but not necessarily for
     * {@code Class.forName}, which accepts a {@code null} ClassLoader
     * reference as well).
     * @return the default ClassLoader (only {@code null} if even the system
     * ClassLoader isn't accessible)
     * @see Thread#getContextClassLoader()
     * @see ClassLoader#getSystemClassLoader()
     */
    @Nullable
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = Classes.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                }
                catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    /**
     * Resolve the given resource URL to a {@code java.io.File},
     * i.e. to a file in the file system.
     * @param resourceUrl the resource URL to resolve
     * @param description a description of the original resource that
     * the URL was created for (for example, a class path location)
     * @return a corresponding File object
     * @throws FileNotFoundException if the URL cannot be resolved to
     * a file in the file system
     */
    public static File getFile(URL resourceUrl, String description) throws FileNotFoundException {
        Preconditions.checkNotNull(resourceUrl, "Resource URL must not be null");
        if (!URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) {
            throw new FileNotFoundException(
                    description + " cannot be resolved to absolute file path " +
                            "because it does not reside in the file system: " + resourceUrl);
        }
        try {
            return new File(toURI(resourceUrl).getSchemeSpecificPart());
        }
        catch (URISyntaxException ex) {
            // Fallback for URLs that are not valid URIs (should hardly ever happen).
            return new File(resourceUrl.getFile());
        }
    }

    /**
     * Create a URI instance for the given URL,
     * replacing spaces with "%20" URI encoding first.
     * @param url the URL to convert into a URI instance
     * @return the URI instance
     * @throws URISyntaxException if the URL wasn't a valid URI
     * @see java.net.URL#toURI()
     */
    public static URI toURI(URL url) throws URISyntaxException {
        return toURI(url.toString());
    }

    /**
     * Create a URI instance for the given location String,
     * replacing spaces with "%20" URI encoding first.
     * @param location the location String to convert into a URI instance
     * @return the URI instance
     * @throws URISyntaxException if the location wasn't a valid URI
     */
    public static URI toURI(String location) throws URISyntaxException {
        return new URI(Matchers.SPACE.replaceFrom(location, "%20"));
    }
}
