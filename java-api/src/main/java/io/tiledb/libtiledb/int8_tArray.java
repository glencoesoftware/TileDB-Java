/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package io.tiledb.libtiledb;

public class int8_tArray {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected int8_tArray(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(int8_tArray obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        tiledbJNI.delete_int8_tArray(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public int8_tArray(int nelements) {
    this(tiledbJNI.new_int8_tArray(nelements), true);
  }

  public byte getitem(int index) {
    return tiledbJNI.int8_tArray_getitem(swigCPtr, this, index);
  }

  public void setitem(int index, byte value) {
    tiledbJNI.int8_tArray_setitem(swigCPtr, this, index, value);
  }

  public SWIGTYPE_p_signed_char cast() {
    long cPtr = tiledbJNI.int8_tArray_cast(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_signed_char(cPtr, false);
  }

  public static int8_tArray frompointer(SWIGTYPE_p_signed_char t) {
    long cPtr = tiledbJNI.int8_tArray_frompointer(SWIGTYPE_p_signed_char.getCPtr(t));
    return (cPtr == 0) ? null : new int8_tArray(cPtr, false);
  }

}