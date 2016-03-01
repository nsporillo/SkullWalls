package net.porillo.skullwalls.collections;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Concurrent Hash Set implementation
 * Idea is to pretend to be a hash set when is really a hash map with every value being true
 * Underlying set is the keys of the hash map
 * Provides concurrent iterators (important for invites / members / parties!)
 * @param <E> type
 */
public class ConcurrentHashSet<E> implements ConcurrentSet<E>, Serializable {

    private static final long serialVersionUID = -913526372691027123L;
    private final ConcurrentMap<E, Object> m;
    private transient Set<E> s;

    public ConcurrentHashSet() {
        this.m = new ConcurrentHashMap<>();
        init();
    }

    public ConcurrentHashSet(int initialCapacity) {
        this.m = new ConcurrentHashMap<>(initialCapacity);
        init();
    }

    public ConcurrentHashSet(int initialCapacity, float loadFactor) {
        this.m = new ConcurrentHashMap<>(initialCapacity, loadFactor);
        init();
    }

    public ConcurrentHashSet(int initialCapacity, float loadFactor, int concurrencyLevel) {
        this.m = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrencyLevel);
        init();
    }

    public ConcurrentHashSet(Set<? extends E> s) {
        this(Math.max(Objects.requireNonNull(s).size(), 16));
        addAll(s);
    }

    // New type of constructor
    public ConcurrentHashSet(Supplier<? extends ConcurrentMap<E, Object>> concurrentMapSupplier) {
        final ConcurrentMap<E, Object> newMap = concurrentMapSupplier.get();
        if (newMap == null) {
            throw new IllegalArgumentException("The supplied map does not implement " + ConcurrentMap.class.getSimpleName());
        }
        this.m = newMap;
        init();
    }

    /**
     * Initialize our underlying Set
     * Will contain the keys of the concurrent hash map here after
     */
    private void init() {
        this.s = m.keySet();
    }

    public void clear() {
        m.clear();
    }

    public int size() {
        return m.size();
    }

    public boolean isEmpty() {
        return m.isEmpty();
    }

    public boolean contains(Object o) {
        return m.containsKey(o);
    }

    public boolean remove(Object o) {
        return m.remove(o) != null;
    }

    public boolean add(E e) {
        return m.put(e, Boolean.TRUE) == null;
    }

    public Iterator<E> iterator() {
        return s.iterator();
    }

    public Object[] toArray() {
        return s.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return s.toArray(a);
    }

    public String toString() {
        return s.toString();
    }

    public int hashCode() {
        return s.hashCode();
    }

    public boolean equals(Object o) {
        return s.equals(o);
    }

    public boolean containsAll(Collection<?> c) {
        return s.containsAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        return s.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return s.retainAll(c);
    }

    public boolean addAll(Collection<? extends E> c) {
        return Objects.requireNonNull(c).stream().map(this::add).filter((b) -> b).count() > 0;
    }

    public void forEach(Consumer<? super E> action) {
        s.forEach(action);
    }

    public boolean removeIf(Predicate<? super E> filter) {
        return s.removeIf(filter);
    }

    public Spliterator<E> spliterator() {
        return s.spliterator();
    }

    public Stream<E> stream() {
        return s.stream();
    }

    public Stream<E> parallelStream() {
        return s.parallelStream();
    }
}