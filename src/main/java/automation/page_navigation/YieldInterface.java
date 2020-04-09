package automation.page_navigation;

import java.util.List;

@FunctionalInterface
public interface YieldInterface<T, U, R> {
    List<R> apply(T t, U u);
}
