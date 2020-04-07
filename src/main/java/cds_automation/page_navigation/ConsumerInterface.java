package cds_automation.page_navigation;

@FunctionalInterface
public interface ConsumerInterface<T, U> {
    void apply(T t, U u);
}
