package com.yafnds.en;

/**
 * 类描述：数字字典 名词检索 枚举类
 * <p>创建时间：2022/10/7/007 9:08
 *
 * @author yafnds
 * @version 1.0
 */

public enum DictCode {

    /** 根节点 */
    ROOT("ROOT"),
    /** 户型 */
    HOUSETYPE("houseType"),
    /** 楼层 */
    FLOOR("floor"),
    /** 建筑结构 */
    BUILDSTRUCTURE("buildStructure"),
    /** 装修情况 */
    DECORATION("decoration"),
    /** 朝向 */
    DIRECTION("direction"),
    /** 房源用途 */
    HOUSEUSE("houseUse"),
    /** 省 */
    PROVINCE("province"),
    /** 北京 */
    BEIJING("beijing");

    public String code;

    DictCode(String code) {
        this.code = code;
    }
}
