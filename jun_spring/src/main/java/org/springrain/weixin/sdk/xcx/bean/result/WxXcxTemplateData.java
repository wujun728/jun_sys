package org.springrain.weixin.sdk.xcx.bean.result;

import java.io.Serializable;

/**
 * @author Wujun
 */
public class WxXcxTemplateData implements Serializable {

  private static final long serialVersionUID = 6301835292940277870L;
  private String name;
  private String value;
  private String color;

  public WxXcxTemplateData() {
  }

  public WxXcxTemplateData(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public WxXcxTemplateData(String name, String value, String color) {
    this.name = name;
    this.value = value;
    this.color = color;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getColor() {
    return this.color;
  }

  public void setColor(String color) {
    this.color = color;
  }

}
