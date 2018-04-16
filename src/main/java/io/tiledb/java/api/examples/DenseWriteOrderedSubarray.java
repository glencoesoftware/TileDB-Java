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
 * It shows how to write to a dense subarray, providing the array cells ordered
 * in row-major order within the specified subarray. TileDB will properly
 * re-organize the cells into the global cell order, prior to writing them
 * on the disk.
 *
 * Make sure that there is no directory named `my_dense_array` in your
 * current working directory.
 *
 * You need to run the following to make it work:
 *
 * DenseCreate
 * DenseWriteOrderedSubarray

 */

package io.tiledb.java.api.examples;

import io.tiledb.java.api.Array;
import io.tiledb.java.api.Context;
import io.tiledb.java.api.Query;
import io.tiledb.java.api.TileDBError;
import io.tiledb.libtiledb.tiledb_layout_t;
import io.tiledb.libtiledb.tiledb_query_type_t;

public class DenseWriteOrderedSubarray {
  public static void main(String[] args) throws TileDBError {

    // Create TileDB context
    Context ctx = new Context();

    // Prepare cell buffers
    int[] a1_data = {9, 12, 13, 11, 14, 15};
    long[] a2_offsets = {0, 2, 3, 5, 9, 12};
    String buffer_var_a2 = "jjmnnllllooopppp";
    float buffer_a3[] = {9.1f, 9.2f, 12.1f, 12.2f, 13.1f, 13.2f, 11.1f, 11.2f, 14.1f, 14.2f, 15.1f, 15.2f};

    // Create query
    Array my_dense_array = new Array(ctx,"my_dense_array");
    Query query = new Query(my_dense_array, tiledb_query_type_t.TILEDB_WRITE);
    query.set_layout(tiledb_layout_t.TILEDB_ROW_MAJOR);
    query.set_subarray(new long[] {3, 4, 2, 4});
    query.set_buffer("a1", a1_data, a1_data.length);
    query.set_buffer("a2", a2_offsets, a2_offsets.length, buffer_var_a2.getBytes(), buffer_var_a2.length());
    query.set_buffer("a3", buffer_a3, buffer_a3.length);

    // Submit query
    query.submit();
    query.free();
  }
}