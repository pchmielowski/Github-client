package net.chmielowski.github;

public final class TestCustomApplication extends CustomApplication {
    @Override
    protected boolean isTest() {
        return true;
    }
}
