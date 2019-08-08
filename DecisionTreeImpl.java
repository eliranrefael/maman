import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Fill in the implementation details of the class DecisionTree using this file.
 * Any methods or secondary classes that you want are fine but we will only
 * interact with those methods in the DecisionTree framework.
 * 
 * You must add code for the 1 member and 4 methods specified below.
 * 
 * See DecisionTree for a description of default methods.
 */
//Eliran Refael 300758190

public class DecisionTreeImpl extends DecisionTree {
  private DecTreeNode root;
  // ordered list of class labels
  private List<String> labels;
  // ordered list of attributes
  private List<String> attributes;
  // map to ordered discrete values taken by attributes
  private Map<String, List<String>> attributeValues;

  /**
   * Answers static questions about decision trees.
   */
  DecisionTreeImpl() {
    // no code necessary this is void purposefully
  }

  /**
   * Build a decision tree given only a training set.
   * 
   * @param train: the training set
   */
  DecisionTreeImpl(DataSet train) {

    this.labels = train.labels;
    this.attributes = train.attributes;
    this.attributeValues = train.attributeValues;

    root = DecisionTreeLearning(train.instances, this.attributes, null, train.instances);

    /*
     * Map<String, Integer> atr=new HashMap<String, Integer>(); for(String
     * i:this.attributes) atr.put(i, 0);
     * 
     * for(int i=0;i<this.attributes.size();i++){ int x=bestAtr(train);
     * if(root=null) for(int j=0;j<this.attributeValues.get(x).size();j++)
     * root.addChild(new DecTreeNode(String _label, String _attribute, String
     * _parentAttributeValue, boolean _terminal); }
     */

    // TODO: add code here
  }

  @Override
  public String classify(Instance instance) {

    // TODO: add code here
    return "x";
  }

  @Override
  public void rootInfoGain(DataSet train) {
    this.labels = train.labels;
    this.attributes = train.attributes;
    this.attributeValues = train.attributeValues;
    // TODO: add code here
  }

  @Override
  public void printAccuracy(DataSet test) {
    // TODO: add code here
    return;
  }

  /**
   * Build a decision tree given a training set then prune it using a tuning set.
   * ONLY for extra credits
   * 
   * @param train: the training set
   * @param tune:  the tuning set
   */
  DecisionTreeImpl(DataSet train, DataSet tune) {

    this.labels = train.labels;
    this.attributes = train.attributes;
    this.attributeValues = train.attributeValues;
    // TODO: add code here
    // only for extra credits
  }

  @Override
  /**
   * Print the decision tree in the specified format
   */
  public void print() {

    printTreeNode(root, null, 0);
  }

  /**
   * Prints the subtree of the node with each line prefixed by 4 * k spaces.
   */
  public void printTreeNode(DecTreeNode p, DecTreeNode parent, int k) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < k; i++) {
      sb.append("    ");
    }
    String value;
    if (parent == null) {
      value = "ROOT";
    } else {
      int attributeValueIndex = this.getAttributeValueIndex(parent.attribute, p.parentAttributeValue);
      value = attributeValues.get(parent.attribute).get(attributeValueIndex);
    }
    sb.append(value);
    if (p.terminal) {
      sb.append(" (" + p.label + ")");
      System.out.println(sb.toString());
    } else {
      sb.append(" {" + p.attribute + "?}");
      System.out.println(sb.toString());
      for (DecTreeNode child : p.children) {
        printTreeNode(child, p, k + 1);
      }
    }
  }

  /**
   * Helper function to get the index of the label in labels list
   */
  private int getLabelIndex(String label) {
    for (int i = 0; i < this.labels.size(); i++) {
      if (label.equals(this.labels.get(i))) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Helper function to get the index of the attribute in attributes list
   */
  private int getAttributeIndex(String attr) {
    for (int i = 0; i < this.attributes.size(); i++) {
      if (attr.equals(this.attributes.get(i))) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Helper function to get the index of the attributeValue in the list for the
   * attribute key in the attributeValues map
   */
  private int getAttributeValueIndex(String attr, String value) {
    for (int i = 0; i < attributeValues.get(attr).size(); i++) {
      if (value.equals(attributeValues.get(attr).get(i))) {
        return i;
      }
    }
    return -1;
  }

  private DecTreeNode DecisionTreeLearning(List<Instance> examples, List<String> attributes, String parentAtr, List<Instance> parentExmp ){
     if (examples.isEmpty())
      return new DecTreeNode(Plurality(parentExmp),null,parentAtr,true);
    else if(unifiedClass(examples)!=null)
      return new DecTreeNode(unifiedClass(examples),null,parentAtr,true);
    else if(attributes.isEmpty())
      return new DecTreeNode(Plurality(examples),null,parentAtr,true);
    else{
      String sup=Importance(examples,attributes);
      DecTreeNode tree=new DecTreeNode(sup, sup, parentAtr, false);
      List<String> atrVal=this.attributeValues.get(sup);
      List<String> newAtr=new ArrayList<String>(attributes);
      newAtr.remove(sup);
      for(String str:atrVal){
        List<Instance> exs=new ArrayList<Instance>();
      for(Instance inst:examples)
        if(inst.attributes.get(getAttributeIndex(sup)).equals(str))
        exs.add(inst);
      tree.addChild(DecisionTreeLearning(exs,newAtr,sup,examples));
      } 
      return tree;
    }


    
  }

private String Plurality(List<Instance> exmp){
  int[] x=new int[this.labels.size()];
  for(Instance i:exmp)
  x[getLabelIndex(i.label)]+=1;
  int max=0;
  int i=0;
  for(int j=0;j<this.labels.size();j++)
  if(x[j]>max)
  i=j;

  return this.labels.get(i);



}

private String unifiedClass(List<Instance> exmp){
  String str=exmp.get(0).label;
  for (Instance i:exmp)
  if(!(str.equals(i.label)))
  return null;
  return str;

}

private String Importance(List<Instance> exmp,List<String> attributes){
  double atrEnt;
  double sum=0;
  double sumLabel;
  double exmpnum=exmp.size();
  double totalEnt;
  for(Instance inst:exmp)
  if(inst.label.equals(this.labels.get(1)))
    sum++;
    if(sum==0||sum==exmpnum)
    totalEnt=0;
    else
   totalEnt=-(sum/exmpnum)*Math.log(sum/exmpnum)-((exmpnum-sum)/exmpnum)*Math.log((exmpnum-sum)/exmpnum);

  TreeMap<Double,String> atrImportance=new TreeMap<Double,String>();
  for(String atr:attributes){
    atrEnt=totalEnt;
  for(String val:this.attributeValues.get(atr)){
    sum=0;
    sumLabel=0;
  for(Instance inst:exmp){
    if(inst.attributes.get(getAttributeIndex(atr)).equals(val)&&inst.label.equals(this.labels.get(1))){
    sum++;
    sumLabel++;}
    else if(inst.attributes.get(getAttributeIndex(atr)).equals(val))
    sum++;}
    if(sum==0||sum==sumLabel||sumLabel==0)
    continue;
    double a=(((sumLabel/sum)*Math.log(sumLabel/sum)));
    double b=(((sum-sumLabel)/sum)*Math.log((sum-sumLabel)/sum));
    double c=(((sumLabel/sum)*Math.log(sumLabel/sum))+(((sum-sumLabel)/sum)*Math.log((sum-sumLabel)/sum)));
    double d=(sum/exmpnum);
    atrEnt+=(sum/exmpnum)*(((sumLabel/sum)*Math.log(sumLabel/sum))+(((sum-sumLabel)/sum)*Math.log((sum-sumLabel)/sum)));
  }
  
  atrImportance.put(atrEnt,atr);
}
  return atrImportance.get(atrImportance.lastKey());
  

}

}
