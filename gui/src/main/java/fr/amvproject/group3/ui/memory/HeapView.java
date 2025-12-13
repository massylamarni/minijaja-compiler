package fr.amvproject.group3.ui.memory;

import java.util.ArrayList;
import java.util.List;

import fr.amvproject.group3.Heap;
import fr.amvproject.group3.model.LangWrapper;
import fr.amvproject.group3.nodes.ArrayInfoNode;
import fr.amvproject.group3.nodes.PointerNode;
import fr.amvproject.group3.ui.memory.nodes.HeapItemView;
import javafx.scene.layout.VBox;

public class HeapView extends VBox {
  public HeapView(LangWrapper langWrapper) {
    langWrapper.setHeapView(this);

    this.getStyleClass().add("heap-view");
  }

  public void buildHeap(Heap heap) {
    this.getChildren().clear();

    List<HeapItemView> heapItemViews = new ArrayList<>();
    for (Object value : heap.getQueue()) {
      if (value instanceof ArrayInfoNode arrayInfoNode) {
        heapItemViews.add(new HeapItemView("#", String.valueOf(arrayInfoNode.getSize())));
      } else if (value instanceof PointerNode pointerNode) {
        heapItemViews.add(new HeapItemView("*", String.valueOf(pointerNode.getAddress())));
      } else {
        heapItemViews.add(new HeapItemView("@", String.valueOf(value)));
      }
    }
    this.getChildren().addAll(heapItemViews);
  }
}
