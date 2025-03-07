package Helper;
import java.util.List;
import java.util.function.Predicate;

public class GenericFinder {
    // 리스트에서 특정 조건을 만족하는 첫 번째 요소를 찾는 제네릭 메서드
    public static <T, R> R findInList(List<T> list, Predicate<T> condition, Transformer<T, R> transformer) {
        for (T item : list) {
            if (condition.test(item)) {
                return transformer.transform(item); // 변환 후 리턴
            }
        }
        return null; // 찾지 못하면 null 반환
    }

    // 변환을 위한 함수형 인터페이스
    @FunctionalInterface
    public interface Transformer<T, R> {
        R transform(T item);
    }
}
