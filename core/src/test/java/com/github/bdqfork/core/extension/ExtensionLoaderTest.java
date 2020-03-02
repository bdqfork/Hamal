package com.github.bdqfork.core.extension;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExtensionLoaderTest {

    @Test
    public void getExtension() {
        ExtensionLoader<IExtensionTest> extensionLoader = ExtensionLoader.getExtensionLoader(IExtensionTest.class);
        IExtensionTest iExtensionTest = extensionLoader.getExtension("imp1");
        assert iExtensionTest != null;
    }

}