package pl.matsuo.core.util.collection;


import com.google.common.collect.ObjectArrays;


public class ArrayUtil {


  public static <E> E last(E ... array) {
    return array[array.length - 1];
  }


  public static <E> E[] merge(E[] baseArray, E ... additionalElements) {
    return ObjectArrays.concat(baseArray, additionalElements, (Class<E>) baseArray.getClass().getComponentType());
  }
}

