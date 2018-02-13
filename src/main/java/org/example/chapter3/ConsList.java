package org.example.chapter3;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.example.Util.error;
import static org.example.Util.notImplemented;

public abstract class ConsList<A> {

    public static <A> ConsList<A> empty() {
        return new Nil<>();
    }

    public static <A> ConsList<A> cons(A head, ConsList<A> tail) {
        return new Cons<>(head, tail);
    }

    public abstract boolean isEmpty();

    public abstract A head();

    public abstract ConsList<A> tail();

    public int length() {
        return foldLeft((x, y) -> x + 1, 0);
    }

    public ConsList<A> append(A a) {
        return foldRight((x, y) -> cons(x, y), cons(a, empty()));
    }

    public ConsList<A> reverse() {
        return foldLeft((x, y) -> cons(y, x), empty());
    }

    public ConsList<A> takeWhile(Predicate<A> predicate) {
        return foldRight((x, y) -> predicate.test(x) ? cons(x, y) : empty(), empty());
    }

    public ConsList<A> filter(Predicate<A> predicate) {
        return foldRight((x, y) -> predicate.test(x) ? cons(x, y) : y, empty());
    }

    public ConsList<A> filterLeft(Predicate<A> predicate) {
        return foldLeft((x, y) -> predicate.test(y) ? cons(y, x).reverse() : x, empty());
    }

    public <B> ConsList<B> map(Function<A, B> function) {
        return foldRight((x, y) -> cons(function.apply(x), y), empty());
    }

    public <B> ConsList<B> mapLeft(Function<A, B> function)
    {
        return foldLeft((y, x) -> y.append(function.apply(x)), empty());
    }

    public Boolean forAll(Function<A, Boolean> condition) {
        return foldRight((x, y) -> condition.apply(x) && y, true);
    }

    public Boolean forAllLeft(Function<A, Boolean> condition) {
        return foldLeft((y, x) -> condition.apply(x) && y, true);
    }

    public Boolean exists(Function<A, Boolean> condition) {
        return foldRight((x, y) -> condition.apply(x) || y, false);
    }

    public Boolean existsLeft(Function<A, Boolean> condition) {
        return foldLeft((y, x) -> condition.apply(x) || y, false);
    }

    public <B> B foldLeft(BiFunction<B, A, B> f, B z) {
        B res = z;
        ConsList<A> xs = this;
        while (!xs.isEmpty()) {
            res = f.apply(res, xs.head());
            xs = xs.tail();
        }

        return res;
    }

    public <B> B foldRight(BiFunction<A, B, B> f, B z) {
        return isEmpty() ? z : f.apply(head(), tail().foldRight(f, z));

    }

    public <B> B foldRightC(BiFunction<B, A, B> f, B z) {
        return reverse().foldLeft(f, z);

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsList<A> thisIter = this;
        ConsList<A> otherIter = (ConsList<A>) o;

        if (this.length() != otherIter.length()) return false;

        while (!thisIter.isEmpty()) {
            if (thisIter.head() == otherIter.head()) {
                thisIter = thisIter.tail();
                otherIter = otherIter.tail();
            } else {
                return false;
            }
        }

        return true;
    }

    private static final class Cons<A> extends ConsList<A> {
        public final A head;
        public final ConsList<A> tail;

        public Cons(A head, ConsList<A> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public A head() {
            return head;
        }

        @Override
        public ConsList<A> tail() {
            return tail;
        }

        @Override
        public String toString() {
            return "Cons{" + "head=" + head + ", tail=" + tail + '}';
        }
    }


    private static final class Nil<A> extends ConsList<A> {

        public Nil() {
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public A head() {
            throw error("head() on Nil");
        }

        @Override
        public ConsList<A> tail() {
            throw error("tail() on Nil");
        }

        @Override
        public String toString() {
            return "Nil()";
        }
    }
}


