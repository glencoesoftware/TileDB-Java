/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 TileDB, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * @section DESCRIPTION
 *
 * It shows how to read a complete dense array in the global cell order.
 *
 * You need to run the following to make it work:
 *
 * $ DenseCreate
 * $ DenseWriteGlobal1
 * $ DenseReadGlobal
 */

package io.tiledb.java.api.examples;

import io.tiledb.java.api.*;
import io.tiledb.libtiledb.tiledb_layout_t;
import io.tiledb.libtiledb.tiledb_query_type_t;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DenseReadGlobal {
  public static void main(String[] args) throws TileDBError {
    // Create TileDB context
    Context ctx = new Context();

    // Print non-empty domain
    Array my_dense_array = new Array(ctx, "my_dense_array");
    HashMap<String, Pair> dom = my_dense_array.non_empty_domain();
    for (Map.Entry<String, Pair> e : dom.entrySet()){
      System.out.println(e.getKey() + ": ("+e.getValue().getFirst()+", "+e.getValue().getSecond()+")");
    }

    // Print maximum buffer elements for the query results per attribute
    long[] subarray = {1l, 4l, 1l, 4l};
    HashMap<String, Pair<Long,Long>> max_sizes = my_dense_array.max_buffer_elements(subarray, subarray.length);
    for (Map.Entry<String, Pair<Long,Long>> e : max_sizes.entrySet()){
      System.out.println(e.getKey() + " ("+e.getValue().getFirst()+", "+e.getValue().getSecond()+")");
    }

    // Create query
    Query query = new Query(my_dense_array, tiledb_query_type_t.TILEDB_READ);
    query.set_layout(tiledb_layout_t.TILEDB_GLOBAL_ORDER);
    query.set_subarray(subarray);
    query.set_buffer("a1", null,max_sizes.get("a1").getSecond().intValue());
    query.set_buffer("a2",
        null, max_sizes.get("a2").getFirst().intValue(),
        null,max_sizes.get("a2").getSecond().intValue());
    query.set_buffer("a3", null,max_sizes.get("a3").getSecond().intValue());

    // Submit query
    System.out.println("Query submitted: " + query.submit() );

    // Print cell values (assumes all attributes are read)
    HashMap<String, Pair<Long, Long>> result_el = query.result_buffer_elements();
    int[] a1_buff = (int[]) query.get_buffer("a1");
    long[] a2_offsets = (long[]) query.get_var_buffer("a2");
    byte[] a2_data = (byte[]) query.get_buffer("a2");
    float[] a3_buff = (float[]) query.get_buffer("a3");
    for (int i =0; i< a1_buff.length; i++){
      int end = (i==a1_buff.length-1)? a2_data.length : (int) a2_offsets[i+1];
      System.out.println(a1_buff[i] +", "+
          new String(Arrays.copyOfRange(a2_data, (int) a2_offsets[i], end))
          +", ["+a3_buff[2*i]+","+a3_buff[2*i+1]+"]");
    }
  }
}