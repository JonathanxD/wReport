/*
 *      wReport - An Sponge plugin to report bad players and start a vote kick. <https://github.com/JonathanxD/io.github.jonathanxd.wreport.wReport/>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2016 TheRealBuggy/JonathanxD (Jonathan Ribeiro Lopes) <jonathan.scripter@programmer.net>
 *      Copyright (c) contributors
 *
 *
 *      Permission is hereby granted, free of charge, to any person obtaining a copy
 *      of this software and associated documentation files (the "Software"), to deal
 *      in the Software without restriction, including without limitation the rights
 *      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *      copies of the Software, and to permit persons to whom the Software is
 *      furnished to do so, subject to the following conditions:
 *
 *      The above copyright notice and this permission notice shall be included in
 *      all copies or substantial portions of the Software.
 *
 *      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *      THE SOFTWARE.
 */
package io.github.jonathanxd.wreport.list;

import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import io.github.jonathanxd.wreport.reports.Report;

/**
 * Created by jonathan on 03/04/16.
 */
public class ReportList implements List<Report> {

    final List<Report> real = new ArrayList<>();

    @Override
    public int size() {
        return real.size();
    }

    @Override
    public boolean isEmpty() {
        return real.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return real.contains(o);
    }

    @Override
    public Iterator<Report> iterator() {
        return real.iterator();
    }

    @Override
    public Object[] toArray() {
        return real.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return real.toArray(a);
    }

    @Override
    public boolean add(Report report) {

        test(report);

        return real.add(report);
    }

    private Optional<Report> get(long id) {
        return real.stream().filter(c -> c.getId() == id).findAny();
    }

    @Override
    public boolean remove(Object o) {
        return real.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return real.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Report> c) {

        boolean ok = false;
        for(Report r : c) {
            if(add(r) && !ok) {
                ok = true;
            }
        }

        return ok;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Report> c) {

        int i = index;
        boolean ok = false;
        for(Report r : c) {
            try{
                add(i, r);

                if(!ok)
                    ok = true;
            }catch (Throwable t) {
                throw new RuntimeException(t);
            }
            ++i;
        }

        return ok;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return real.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return real.retainAll(c);
    }

    @Override
    public void clear() {
        real.clear();
    }

    @Override
    public Report get(int index) {
        return real.get(index);
    }

    @Override
    public Report set(int index, Report element) {

        test(element);

        return real.set(index, element);
    }

    private void test(Report report) {
        if(get(report.getId()).isPresent()) {
            throw new RuntimeException("Report id "+report.getId()+" already exists!");
        }
    }

    @Override
    public void add(int index, Report element) {
        test(element);

        real.add(index, element);
    }

    @Override
    public Report remove(int index) {
        return real.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return real.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return real.lastIndexOf(o);
    }

    @Override
    public ListIterator<Report> listIterator() {
        return new ListItr(real.listIterator());
    }

    @Override
    public ListIterator<Report> listIterator(int index) {
        return new ListItr(real.listIterator(index));
    }

    @Override
    public List<Report> subList(int fromIndex, int toIndex) {

        ReportList reports = new ReportList();

        reports.addAll(real.subList(fromIndex, toIndex));

        return reports;
    }

    private class ListItr implements ListIterator<Report> {

        private final ListIterator<Report> lastRet;

        ListItr(ListIterator<Report> lastRet) {
            super();
            this.lastRet = lastRet;
        }

        @Override
        public boolean hasNext() {
            return lastRet.hasNext();
        }

        @Override
        public Report next() {
            return lastRet.next();
        }

        public boolean hasPrevious() {
            return lastRet.hasPrevious();
        }

        public int nextIndex() {
            return lastRet.nextIndex();
        }

        public int previousIndex() {
            return lastRet.previousIndex();
        }

        @Override
        public void remove() {
            lastRet.remove();
        }

        public Report previous() {
            return lastRet.previous();
        }

        public void set(Report e) {
            throw new NotImplementedException("set");
        }

        public void add(Report e) {
            throw new NotImplementedException("add");
        }
    }
}
