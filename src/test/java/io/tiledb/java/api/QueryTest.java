package io.tiledb.java.api;

import static io.tiledb.java.api.ArrayType.TILEDB_DENSE;
import static io.tiledb.java.api.Layout.TILEDB_ROW_MAJOR;
import static io.tiledb.java.api.QueryType.TILEDB_READ;
import static io.tiledb.java.api.QueryType.TILEDB_WRITE;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class QueryTest {

  private Context ctx;
  private String arrayURI = "query";

  @Before
  public void setup() throws Exception {
    ctx = new Context();
    if (Files.exists(Paths.get(arrayURI))) {
      TileDBObject.remove(ctx, arrayURI);
    }
    arrayCreate();
    arrayWrite();
  }

  @After
  public void teardown() throws Exception {
    if (Files.exists(Paths.get(arrayURI))) {
      TileDBObject.remove(ctx, arrayURI);
    }
  }

  @Test
  public void test() throws Exception {
    arrayRead();
  }

  public void arrayCreate() throws Exception {
    // The array will be 4x4 with dimensions "rows" and "cols", with domain [1,4].
    Dimension<Integer> rows =
        new Dimension<Integer>(ctx, "rows", Integer.class, new Pair<Integer, Integer>(1, 4), 2);
    Dimension<Integer> cols =
        new Dimension<Integer>(ctx, "cols", Integer.class, new Pair<Integer, Integer>(1, 4), 2);

    // Create and set getDomain
    Domain domain = new Domain(ctx);
    domain.addDimension(rows);
    domain.addDimension(cols);

    // Add two attributes "a1" and "a2", so each (i,j) cell can store
    // a character on "a1" and a vector of two floats on "a2".
    Attribute a1 = new Attribute(ctx, "a1", Character.class);
    Attribute a2 = new Attribute(ctx, "a2", Float.class);
    a2.setCellValNum(2);

    ArraySchema schema = new ArraySchema(ctx, TILEDB_DENSE);
    schema.setTileOrder(TILEDB_ROW_MAJOR);
    schema.setCellOrder(TILEDB_ROW_MAJOR);
    schema.setDomain(domain);
    schema.addAttribute(a1);
    schema.addAttribute(a2);

    Array.create(arrayURI, schema);
  }

  public void arrayWrite() throws Exception {
    // Prepare cell buffers
    NativeArray a1 = new NativeArray(ctx, "abcdefghijklmnop", String.class);
    NativeArray a2 =
        new NativeArray(
            ctx,
            new float[] {
              0.1f, 0.2f, 1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f,
              4.1f, 4.2f, 5.1f, 5.2f, 6.1f, 6.2f, 7.1f, 7.2f,
              8.1f, 8.2f, 9.1f, 9.2f, 10.1f, 10.2f, 11.1f, 11.2f,
              12.1f, 12.2f, 13.1f, 13.2f, 14.1f, 14.2f, 15.1f, 15.2f
            },
            Float.class);

    // Create query
    Array array = new Array(ctx, arrayURI, TILEDB_WRITE);
    Query query = new Query(array);
    query.setLayout(TILEDB_ROW_MAJOR);
    query.setBuffer("a1", a1);
    query.setBuffer("a2", a2);
    // Submit query
    query.submit();
    query.close();
    array.close();
  }

  private void arrayRead() throws Exception {
    Array array = new Array(ctx, arrayURI, TILEDB_READ);

    // Create query
    Query query = new Query(array, TILEDB_READ);

    // Slice only rows 1, 2 and cols 2, 3, 4
    query.addRange(0, (int) 1, (int) 2);
    query.addRange(1, (int) 2, (int) 4);
    query.setLayout(TILEDB_ROW_MAJOR);

    // Prepare the vector that will hold the result
    // (of size 6 elements for "a1" and 12 elements for "a2" since
    // it stores two floats per cell)

    NativeArray a1Array = new NativeArray(ctx, 6, Character.class);
    NativeArray a2Array = new NativeArray(ctx, 12, Float.class);
    query.setBuffer("a1", a1Array);
    query.setBuffer("a2", a2Array);

    // set the number of buffer elements to a size smaller than the read buffer size
    query.setBufferElements("a1", 3);
    query.setBufferElements("a2", 6);

    // Submit query
    query.submit();

    HashMap<String, Pair<Long, Long>> resultElements = query.resultBufferElements();

    Assert.assertEquals(Long.valueOf(3), resultElements.get("a1").getSecond());
    Assert.assertEquals(Long.valueOf(6), resultElements.get("a2").getSecond());

    byte[] a1 = (byte[]) query.getBuffer("a1");
    float[] a2 = (float[]) query.getBuffer("a2");

    Assert.assertArrayEquals(new byte[] {'b', 'c', 'd'}, a1);
    float[] expected_a2 = new float[] {1.1f, 1.2f, 2.1f, 2.2f, 3.1f, 3.2f};
    Assert.assertArrayEquals(expected_a2, a2, 0.01f);

    a1Array.close();
    a2Array.close();
    query.close();
    array.close();
  }
}
