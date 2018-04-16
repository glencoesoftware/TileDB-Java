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
 * This example explores the Java API for the array schema.
 *
 */

package io.tiledb.java.api.examples;

import io.tiledb.java.api.*;
import io.tiledb.libtiledb.tiledb;
import io.tiledb.libtiledb.tiledb_array_type_t;
import io.tiledb.libtiledb.tiledb_compressor_t;
import io.tiledb.libtiledb.tiledb_layout_t;

import java.util.List;
import java.util.Map;

public class ArraySchemaExample {
  public static void main(String[] args) throws TileDBError {
    // Create TileDB context
    Context ctx = new Context();

    // Create array schema
    ArraySchema schema = new ArraySchema(ctx, tiledb_array_type_t.TILEDB_SPARSE);
    schema.set_capacity(10);
    schema.set_tile_order(tiledb_layout_t.TILEDB_ROW_MAJOR);
    schema.set_cell_order(tiledb_layout_t.TILEDB_COL_MAJOR);
    schema.set_coords_compressor(new Compressor(tiledb_compressor_t.TILEDB_ZSTD, 4));
    schema.set_offsets_compressor(new Compressor(tiledb_compressor_t.TILEDB_BLOSC, 5));

    // Create dimensions
    Dimension<Long> d1 = new Dimension<Long>(ctx, "d1", Long.class, new Pair<Long, Long>(1l, 1000l), 10l);
    Dimension<Long> d2 = new Dimension<Long>(ctx, "d2", Long.class, new Pair<Long, Long>(101l, 10000l), 100l);

    // Create and set domain
    Domain<Integer> domain = new Domain<Integer>(ctx);
    domain.add_dimension(d1);
    domain.add_dimension(d2);
    schema.set_domain(domain);

    // Create and add attributes
    Attribute<Integer> a1 = new Attribute<Integer>(ctx, "a1", Integer.class);
    a1.setCellValNum(3);
    Attribute<Float> a2 = new Attribute<Float>(ctx, "a2", Float.class);
    a2.setCompressor(new Compressor(tiledb_compressor_t.TILEDB_GZIP, -1));
    schema.add_attribute(a1);
    schema.add_attribute(a2);

    try {
      schema.check();
    } catch (Exception e) {
      e.printStackTrace();
    }

    schema.dump();

    // Print from getters
    System.out.println("\nFrom getters:"
        + "\n- Array type: "
        + ((schema.getArrayType() == tiledb_array_type_t.TILEDB_DENSE) ? "dense" : "sparse")
        + "\n- Cell order: "
        + (schema.cell_order() == tiledb_layout_t.TILEDB_ROW_MAJOR ? "row-major" :
        "col-major")
        + "\n- Tile order: "
        + (schema.tile_order() == tiledb_layout_t.TILEDB_ROW_MAJOR ? "row-major" :
        "col-major")
        + "\n- Capacity: " + schema.capacity()
        + "\n- Coordinates compressor: " + schema.coords_compressor()
        + "\n- Offsets compressor: " + schema.offsets_compressor());

    // Print the attribute names
    System.out.println("\n\nArray schema attribute names: ");
    for (Map.Entry<String,Attribute> a : schema.attributes().entrySet()) {
      System.out.println("* " +a.getKey());
    }

    // Print domain
    schema.domain().dump();

    // Print the dimension names using iterators
    System.out.println("\nArray schema dimension names: ");
    for (Dimension d: (List<Dimension>) schema.domain().dimensions()) {
      System.out.println("* " + d.getName() );
    }

  }
}