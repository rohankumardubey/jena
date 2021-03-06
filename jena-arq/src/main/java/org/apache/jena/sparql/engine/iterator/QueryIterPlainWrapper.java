/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.sparql.engine.iterator;

import java.util.Iterator ;

import org.apache.jena.atlas.io.IndentedWriter ;
import org.apache.jena.atlas.iterator.Iter ;
import org.apache.jena.atlas.lib.Lib ;
import org.apache.jena.sparql.engine.ExecutionContext ;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding ;
import org.apache.jena.sparql.serializer.SerializationContext ;

/** Turn an normal java.util.Iterator (of Bindings) into a QueryIterator */
public class QueryIterPlainWrapper extends QueryIter
{
    public static QueryIterator create(Iterator<Binding> iter, ExecutionContext execCxt) {
        return new QueryIterPlainWrapper(iter, execCxt);
    }

    public static QueryIterator create(Iterator<Binding> iter) {
        return new QueryIterPlainWrapper(iter);
    }

    private Iterator<Binding> iterator = null;

    protected QueryIterPlainWrapper(Iterator<Binding> iter)
    { this(iter, null); }

    protected QueryIterPlainWrapper(Iterator<Binding> iter, ExecutionContext context) {
        super(context);
        iterator = iter;
    }

    /** Preferable to use a constructor - but sometimes that is inconvenient
     *  so pass null in the constructor and then call this before the iterator is
     *  used.
     */
    public void setIterator(Iterator<Binding> iterator) { this.iterator = iterator; }

    @Override
    protected boolean hasNextBinding() { return iterator.hasNext(); }

    @Override
    protected Binding moveToNextBinding() { return iterator.next(); }

    @Override
    protected void closeIterator() {
        if ( iterator != null ) {
            // In case we wrapped a QueryIterator or a Jena graph ExtendedIterator.
            // Includes the effect of NiceIterator.close(iterator)
            Iter.close(iterator);
            iterator = null;
        }
    }

    @Override
    protected void requestCancel()
    { }

    @Override
    public void output(IndentedWriter out, SerializationContext sCxt)
    { out.println(Lib.className(this)); }
}
