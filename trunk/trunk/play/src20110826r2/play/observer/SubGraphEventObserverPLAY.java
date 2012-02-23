package play.observer;

import higraph.view.ComponentView;
import higraph.view.interfaces.SubgraphEventObserver;

import java.awt.Color;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.Icon;
import javax.swing.TransferHandler;

import play.model.EdgePLAY;
import play.model.EdgePayloadPLAY;
import play.model.NodePLAY;
import play.model.NodePayloadPLAY;
import play.model.SubGraphPLAY;
import play.model.WholeGraphPLAY;
import play.tags.PLAYTags;
import play.view.DropZonePLAY;
import play.view.NodeViewPLAY;
import play.view.SubGraphViewPLAY;
import play.view.ViewFactoryPLAY;

/**
 * @author  Charles
 */
public class SubGraphEventObserverPLAY
		extends
		SubgraphEventObserver<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> {

	/**
	 * @uml.property  name="selectedView"
	 * @uml.associationEnd  
	 */
	public ComponentView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> selectedView = null;
	/**
	 * @uml.property  name="sgv"
	 * @uml.associationEnd  
	 */
	public SubGraphViewPLAY sgv = null;
	/**
	 * @uml.property  name="wg"
	 * @uml.associationEnd  
	 */
	public WholeGraphPLAY wg = null;
	/**
	 * @uml.property  name="vf"
	 * @uml.associationEnd  
	 */
	public ViewFactoryPLAY vf = null;
	/**
	 * @uml.property  name="sg"
	 * @uml.associationEnd  
	 */
	public SubGraphPLAY sg = null;

	public SubGraphEventObserverPLAY(SubGraphViewPLAY sgv, WholeGraphPLAY wg,
			ViewFactoryPLAY vf, SubGraphPLAY sg) {
		this.sgv = sgv;
		this.wg = wg;
		this.vf = vf;
		this.sg = sg;
	}

	@Override
	public void movedOver(
			Stack<ComponentView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY>> stack,
			MouseEvent e) {
	}

	@Override
	public void dragged(
			Stack<ComponentView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY>> stack,
			MouseEvent e) {
		System.out.println("dragged");
	}

	@Override
	public void clickedOn(
			Stack<ComponentView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY>> stack,
			MouseEvent e) {
		System.out.println("clickedOn");
		if (stack != null && !stack.isEmpty()) {
			selectedView = stack.lastElement();
		} else {
			selectedView = null;
		}
	}

	@Override
	public void pressedOn(
			Stack<ComponentView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY>> stack,
			MouseEvent e) {
		System.out.println("pressedOn");
		if (stack != null && !stack.isEmpty()) {

			if (selectedView == null) {
				selectedView = stack.lastElement();
				if (selectedView instanceof NodeViewPLAY) {
					selectedView.setFillColor(Color.green);
					selectedView.refresh();

				}
			} else {
				if (selectedView instanceof NodeViewPLAY) {
					selectedView.setFillColor(Color.white);
					selectedView.refresh();
				}
				selectedView = stack.lastElement();
				if (selectedView instanceof NodeViewPLAY) {
					NodeViewPLAY n = (NodeViewPLAY) selectedView;
					System.out.println("selected tag is: "
							+ n.getNode().getTag());
					selectedView.setFillColor(Color.green);
					selectedView.refresh();
				}
			}
		} else {
			if (selectedView instanceof NodeViewPLAY) {
				selectedView.setFillColor(Color.white);
				selectedView.refresh();
			}
			selectedView = null;
		}
	}

	@Override
	public void releasedOn(
			Stack<ComponentView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY>> stack,
			MouseEvent e) {
		System.out.println("releasedOn");
	}

	@Override
	public int getSourceActions() {
		int action;
		if (selectedView == null) {
			action = DnDConstants.ACTION_NONE;
		} else {
			action = DnDConstants.ACTION_COPY_OR_MOVE;
		}
		System.out.println("getSourseActions = " + action);
		return action;
	}

	@Override
	public Transferable createTransferable() {
		Transferable result;
		if (selectedView == null) {
			result = null;
		} else {
			if (selectedView instanceof NodeViewPLAY) {
				NodeViewPLAY nvp = (NodeViewPLAY) selectedView;
				result = new ViewTransferObjectPLAY(nvp);
			} else {
				result = null;
			}
		}
		System.out.println("createTrasferable");
		return result;
	}

	/**
	 * canDropHere finishes all checking
	 */

	@Override
	public boolean canDropHere(
			Stack<ComponentView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY>> stack,
			TransferHandler.TransferSupport supportObj) {
		System.out.println("Can Drop Here?");
		NodeViewPLAY targetNV = null;
		PLAYTags targetTag = null;
		boolean result = false;

		if ((!supportObj
				.isDataFlavorSupported(ViewTransferObjectPLAY.theViewDataFlavor))) {
			if ((!supportObj
					.isDataFlavorSupported(ViewTransferObjectPLAY.theTagDataFlavor))) {
				System.out.println("it doesn't support Data Flavor");
				return false;
			}
		}

		// check if the selected view is a node
		try {
			if (supportObj.getTransferable().getTransferData(
					ViewTransferObjectPLAY.theTagDataFlavor) != null) {
				targetTag = (PLAYTags) supportObj.getTransferable()
						.getTransferData(
								ViewTransferObjectPLAY.theTagDataFlavor);
			} else
				targetNV = (NodeViewPLAY) supportObj.getTransferable()
						.getTransferData(
								ViewTransferObjectPLAY.theViewDataFlavor);
		} catch (Exception e) {
			return false;
		}

		if (stack.isEmpty() && targetNV != null) {
			return targetNV.getNode().canDetach();
		}
		if (stack.isEmpty() && targetTag != null) {
			return true;
		}

		if (targetNV == null && targetTag == null) {
			System.out.println("both nv and tag are null");
			result = false;
		}

		if (targetNV != null && targetTag == null) {
			System.out.println("NV isn't null, tag is null");
			// check if the content model is allowing this drop
			if (stack.lastElement() instanceof DropZonePLAY) {
				DropZonePLAY dz = (DropZonePLAY) stack.lastElement();
				NodeViewPLAY parentNV = dz.getAssociatedNode();
				NodePLAY parentNode = parentNV.getNode();

				int position = dz.getIndex();
				System.out.println("dz index is:  " + dz.getIndex());

				if (supportObj.getUserDropAction() == DnDConstants.ACTION_MOVE) {
					result = parentNode.canInsertChild(position, targetNV
							.getNode().getTag())
							&& targetNV.getNode().canDetach();
					System.out.println("is it moving to a DropZone?   "
							+ result);
					System.out.println(parentNode.getTag());
					return result;

				} else if (supportObj.getUserDropAction() == DnDConstants.ACTION_COPY) {
					result = parentNode.canInsertChild(position, targetNV
							.getNode().getTag());
					System.out.println("is it copying to a DropZone?   "
							+ result);
					return result;
				} else
					return false;
			} else if (stack.lastElement() instanceof NodeViewPLAY) {
				NodeViewPLAY nv = (NodeViewPLAY) stack.lastElement();
				NodePLAY node = nv.getNode();

				int position = node.findIndex();
				System.out.println("node index is :   " + position);

				if (supportObj.getUserDropAction() == DnDConstants.ACTION_MOVE) {
					result = node.canReplace(targetNV.getNode().getTag())
							&& targetNV.getNode().canDetach();
					System.out.println("is it moving to a NodeView?   "
							+ result);
					System.out.println(node.getTag());
					return result;

				} else if (supportObj.getUserDropAction() == DnDConstants.ACTION_COPY) {
					result = node.canReplace(targetNV.getNode().getTag());
					System.out.println("is it copying to a NodeView?   "
							+ result);
					return result;

				} else
					return false;
			} else
				return false;
		}
		if (targetTag != null && targetNV == null) {
			System.out.println("tag isn't null, nv is null");
			if (stack.lastElement() instanceof DropZonePLAY) {
				DropZonePLAY dz = (DropZonePLAY) stack.lastElement();
				NodeViewPLAY parentNV = dz.getAssociatedNode();
				NodePLAY parentNode = parentNV.getNode();

				int position = dz.getIndex();
				System.out.println("dz index is:  " + dz.getIndex()
						+ "dropping position is" + position);

				result = parentNode.canInsertChild(position, targetTag);
				System.out.println("is it copying to a DropZone?   " + result);
				return result;

			} else if (stack.lastElement() instanceof NodeViewPLAY) {
				NodeViewPLAY nv = (NodeViewPLAY) stack.lastElement();
				NodePLAY node = nv.getNode();

				int position = node.findIndex();
				System.out.println("node index is :   " + position);

				result = node.canReplace(targetTag);
				System.out.println("is it copying to a NodeView?   " + result);
				return result;

			} else
				return false;
		}
		System.out.println("can drop here? ---->" + result);
		return result;
	}

	/**
	 * I do every checking in canDropHere . So importData() just handles data
	 * transferring.
	 */
	@Override
	public boolean importData(
			Stack<ComponentView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY>> stack,
			TransferHandler.TransferSupport support) {
		System.out.println("importData");

		NodeViewPLAY data = null;
		PLAYTags tag = null;

		if (!canDropHere(stack, support)) {
			return false;
		}

		try {
			if (support.getTransferable().getTransferData(
					ViewTransferObjectPLAY.theTagDataFlavor) != null) {
				tag = (PLAYTags) support.getTransferable().getTransferData(
						ViewTransferObjectPLAY.theTagDataFlavor);
			} else
				data = (NodeViewPLAY) support.getTransferable()
						.getTransferData(
								ViewTransferObjectPLAY.theViewDataFlavor);
		} catch (Exception e) {
			return false;
		}
		System.out.println("tag is empty?? -->" + tag);
		System.out.println("data is empty??-->" + data);

		if (support.isDrop()) {
			// from pallet
			if (tag != null) {
				// drop on a Subgraph
				if (stack.isEmpty()) {
					sg.addTop(wg.makeRootNode(tag));
					refreshSGV();
					return true;
					// drop on a DropZone
				} else if (stack.lastElement() instanceof DropZonePLAY) {
					DropZonePLAY dz = (DropZonePLAY) stack.lastElement();
					NodeViewPLAY parent = dz.getAssociatedNode();
					int position = dz.getIndex();
					parent.getNode().insertChild(position, tag);
					refreshSGV();
					return true;
					// drop on a NodeView
				} else if (stack.lastElement() instanceof NodeViewPLAY) {
					NodeViewPLAY replacingNv = (NodeViewPLAY) stack
							.lastElement();
					replacingNv.getNode().replace(tag);
					refreshSGV();
					return true;
				} else {
					return false;
				}
			}
			// from subgraph to subgraph
			if (data != null) {
				// drop on a subgraph
				if (stack.isEmpty()) {
					// from same subgraph
					if (sg.contains(data.getNode())) {
						System.out.println("Contains");
						if (support.getUserDropAction() == DnDConstants.ACTION_COPY) {
							NodePLAY temp = data.getNode().duplicate();
							sg.addTop(temp);
							refreshSGV();
							return true;
						} else if (support.getUserDropAction() == DnDConstants.ACTION_MOVE) {
							data.getNode().detach();
							sg.addTop(data.getNode());
							refreshSGV();
							return true;
						}
						// from different subgraph
					} else {
						System.out.println("NO~Contains");
						data.getNode().detach();
						sg.addTop(data.getNode());
						data.getSGV().getSG().removeTop(data.getNode());
						refreshSGV();
						return true;
					}

				}
				// drop a node on a DropZone
				if (stack.lastElement() instanceof DropZonePLAY) {
					DropZonePLAY dz = (DropZonePLAY) stack.lastElement();
					NodeViewPLAY parent = dz.getAssociatedNode();
					int position = dz.getIndex();
					if (support.getUserDropAction() == DnDConstants.ACTION_MOVE) {
						if (parent == (NodeViewPLAY) data.getParent()) {
							if (position < data.getNode().findIndex()) {
								data.getNode().detach();
								parent.getNode().insertChild(position,
										data.getNode());
								refreshSGV();
								return true;
							} else if (position > data.getNode().findIndex() + 1) {
								data.getNode().detach();
								parent.getNode().insertChild(position - 1,
										data.getNode());
								refreshSGV();
								return true;
							}
						} else {
							data.getNode().detach();
							parent.getNode().insertChild(position,
									data.getNode());
							refreshSGV();
							return true;
						}
					} else if (support.getUserDropAction() == DnDConstants.ACTION_COPY) {
						if (data.getNode().canDuplicate()) {
							NodePLAY duplicateNode = data.getNode().duplicate();
							parent.getNode().insertChild(position,
									duplicateNode);
							refreshSGV();
							return true;
						} else
							return false;
					} else {
						return false;
					}
				}
				// replacing a node to a NodeView
				else if (stack.lastElement() instanceof NodeViewPLAY) {
					NodeViewPLAY replacingNv = (NodeViewPLAY) stack
							.lastElement();
					if (support.getUserDropAction() == DnDConstants.ACTION_MOVE) {
						data.getNode().detach();
						replacingNv.getNode().replace(data.getNode());
						refreshSGV();
						return true;
					} else if (support.getUserDropAction() == DnDConstants.ACTION_COPY) {
						NodePLAY duplicateNode = data.getNode().duplicate();
						replacingNv.getNode().replace(duplicateNode);
						refreshSGV();
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
		return false;
	}

	@Override
	public Icon getVisualRepresentation(Transferable t) {
		return null;
	}

	private void refreshSGV() {
		if (wg != null) {
			for (SubGraphViewPLAY view : wg.getSgvList()) {
				view.refresh();
			}
		}
	}
}
