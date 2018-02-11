package org.example.chapter2;

import java.io.File;
import java.util.function.Function;

import static org.example.Util.error;

public abstract class ConsList<A> {
    public static <A> ConsList<A> empty() {
        return new Nil<>();
    }

    public static <A> ConsList<A> cons(A head, ConsList<A> tail) {
        return new Cons<>(head, tail);
    }

    public abstract int length();

    public abstract ConsList<A> append(A element);

    public abstract ConsList<A> reverse();

    public abstract ConsList<A> takeWhile(Function<A, Boolean> condition);

    public abstract ConsList<A> dropWhile(Function<A, Boolean> condition);

    public abstract ConsList<A> filter(Function<A, Boolean> condition);

    public abstract ConsList<A> map(Function<A, A> condition);

    public abstract boolean isEmpty();

    public abstract A head();

    public abstract ConsList<A> tail();

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsList<A> thisIter = this;
        ConsList<A> otherIter = (ConsList<A>) o;

        if (this.length() != otherIter.length()) return false;

        while (!thisIter.isEmpty())
        {
            if (thisIter.head() == otherIter.head())
            {
                thisIter = thisIter.tail();
                otherIter = otherIter.tail();
            }
            else
            {
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
        public int length() {

            return 1 + tail.length();
        }

        @Override
        public ConsList<A> append(A element) {

            return cons(head, tail.append(element));
        }

        @Override
        public ConsList<A> reverse() {

            return tail.isEmpty() ? this : tail.reverse().append(head);
        }

        @Override
        public ConsList<A> takeWhile(Function<A, Boolean> condition) {

            return !condition.apply(head) ? empty() : cons(head, tail.takeWhile(condition));
        }

        @Override
        public ConsList<A> dropWhile(Function<A, Boolean> condition) {

            return condition.apply(head) ? tail.dropWhile(condition) : this;
        }

        @Override
        public ConsList<A> filter(Function<A, Boolean> condition) {

            return condition.apply(head) ? cons(head, tail.filter(condition)) : tail.filter(condition);
        }

        @Override
        public ConsList<A> map(Function<A, A> function) {

            return cons(function.apply(head), tail.map(function));
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
    }

    private static final class Nil<A> extends ConsList<A> {

        public Nil() {
        }

        @Override
        public int length() {
            return 0;
        }

        @Override
        public ConsList<A> append(A element) {
            return cons(element, empty());
        }

        @Override
        public ConsList<A> reverse() {
            return empty();
        }

        @Override
        public ConsList<A> takeWhile(Function<A, Boolean> condition) {
            return empty();
        }

        @Override
        public ConsList<A> dropWhile(Function<A, Boolean> condition) {
            return empty();
        }

        @Override
        public ConsList<A> filter(Function<A, Boolean> condition) {
            return empty();
        }

        @Override
        public ConsList<A> map(Function<A, A> function) {
            return empty();
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
    }


}


