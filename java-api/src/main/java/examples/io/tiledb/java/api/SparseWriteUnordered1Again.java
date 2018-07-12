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
 * It shows how to write unordered cells to a sparse array in a single write.
 * This time we write 4 cells.
 *
 * You need to run the following to make this work:
 *
 * SparseCreate
 * SparseWriteUnordered1Again
 */

package examples.io.tiledb.java.api;

import io.tiledb.java.api.Array;
import io.tiledb.java.api.Context;
import io.tiledb.java.api.NativeArray;
import io.tiledb.java.api.Query;
import io.tiledb.libtiledb.tiledb_layout_t;
import io.tiledb.libtiledb.tiledb_query_type_t;

public class SparseWriteUnordered1Again {
  public static void main(String[] args) throws Exception {
    // Create TileDB context
    Context ctx = ctx = new Context();

    // Prepare cell buffers
    NativeArray a1_data = new NativeArray(
        ctx,
        new int[] {107, 104, 106, 105},
        Integer.class);
    NativeArray a2_offsets = new NativeArray(
        ctx,
        new long[]{0, 3, 4, 5},
        Long.class);
    NativeArray buffer_var_a2 = new NativeArray(
        ctx,
        "yyy" + "u" + "w" + "vvvv",
        String.class);

    NativeArray buffer_a3 = new NativeArray(
        ctx,
        new float[]{107.1f, 107.2f, 104.1f, 104.2f, 106.1f, 106.2f, 105.1f, 105.2f},
        Float.class);

    NativeArray coords_buff = new NativeArray(
        ctx,
        new long[]{3, 4, 3, 2, 3, 3, 4, 1},
        Long.class);

    // Create query
    Array my_sparse_array = new Array(ctx, "my_sparse_array", tiledb_query_type_t.TILEDB_WRITE);
    Query query = new Query(my_sparse_array);
    query.setLayout(tiledb_layout_t.TILEDB_UNORDERED);
    query.setBuffer("a1", a1_data);
    query.setBuffer("a2", a2_offsets, buffer_var_a2);
    query.setBuffer("a3", buffer_a3);
    query.setCoordinates(coords_buff);

    // Submit query
    query.submit();
  }
}
