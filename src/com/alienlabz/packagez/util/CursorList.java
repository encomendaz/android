package com.alienlabz.packagez.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.database.Cursor;
import android.util.SparseArray;

import com.alienlabz.packagez.model.Model;

/**
 * List que obtém os valores de um Cursor.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 *
 * @param <E>
 */
public class CursorList<E extends Model<?>> implements List<E> {
	protected Cursor cursor;
	protected SparseArray<E> cache = new SparseArray<E>();
	protected boolean useCache = false;
	protected int count;
	private Class<E> clazz;

	public CursorList(final Cursor cursor, final Class<E> clazz) {
		this.cursor = cursor;
		this.clazz = clazz;
		this.count = cursor.getCount();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (!cursor.isClosed()) {
			cursor.close();
		}
	}

	@Override
	final public boolean add(E object) {
		throw new UnsupportedOperationException();
	}

	@Override
	final public void add(int location, E object) {
		throw new UnsupportedOperationException();
	}

	@Override
	final public boolean addAll(Collection<? extends E> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	final public boolean addAll(int arg0, Collection<? extends E> arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	final public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	final public boolean contains(Object object) {
		throw new UnsupportedOperationException();
	}

	@Override
	final public boolean containsAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E get(int location) {
		E result = Reflection.instantiate(clazz);

		if (useCache) {
			result = cache.get(location);
		} else {
			cursor.moveToPosition(location);
			result.fromCursor(cursor);
			cache.put(location, result);

			if (cache.size() == count) {
				useCache = true;
				cursor.close();
			}
		}

		return result;
	}

	@Override
	public int indexOf(Object object) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		return cursor.getCount() <= 0;
	}

	@Override
	public Iterator<E> iterator() {
		return new MyListIterator();
	}

	@Override
	public int lastIndexOf(Object object) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<E> listIterator(int location) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E remove(int location) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object object) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E set(int location, E object) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return count;
	}

	@Override
	public List<E> subList(int start, int end) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T[] toArray(T[] array) {
		throw new UnsupportedOperationException();
	}

	public class MyListIterator implements Iterator<E> {
		private int position = 0;
		private int count = cursor.getCount();

		@Override
		public boolean hasNext() {
			return position < count;
		}

		@Override
		public E next() {
			E result = null;
			if (hasNext()) {
				result = get(position);
				position++;
			}
			return result;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}