/**************************************************************************
 * $RCSfile: CommonSysConst.java,v $  $Revision: 1.12.2.3 $  $Date: 2009/06/12 05:25:27 $
 *
 * $Log: CommonSysConst.java,v $
 * Revision 1.12.2.3  2009/06/12 05:25:27  wangqi
 * *** empty log message ***
 *
 * Revision 1.12.2.2  2008/03/17 10:28:32  wangqi
 * *** empty log message ***
 *
 * Revision 1.12.2.1  2008/03/07 04:14:34  wangqi
 * *** empty log message ***
 *
 * Revision 1.12  2008/01/24 07:39:35  wangqi
 * *** empty log message ***
 *
 * Revision 1.11  2007/11/23 07:15:14  jushi
 * MR#:BIZSERVICE20070806-85 客户所属的运营商添加数据字典“中国电信”
 * add by jushi 2007/10/16
 *
 * Revision 1.10  2007/09/06 11:36:05  wangqi
 * *** empty log message ***
 *
 * Revision 1.9  2007/09/05 06:46:48  wangqi
 * *** empty log message ***
 *
 * Revision 1.8  2007/09/03 01:41:23  wangqi
 * *** empty log message ***
 *
 * Revision 1.7  2007/08/22 09:25:38  wangqi
 * public final static String APPMODULE_BIZM = "BIZM";    //    营销支撑系统
 * public final static String APPMODULE_DATMT = "DATMT";  //    数据维护系统
 *
 * Revision 1.6  2007/06/16 04:30:11  qilin
 * no message
 *
 * Revision 1.5  2007/05/31 07:41:31  qilin
 * code format
 *
 * Revision 1.4  2007/05/30 09:25:32  yuhong
 * MR#:NMBF30-9999 2007/05/30
 *
 * Revision 1.3  2007/05/30 09:18:13  yuhong
 * MR#:NMBF30-9999 2007/05/30
 *     //add by yuhong 2007/05/30
 *     public final static String GLOBAL_JOINTYPE_XOR="XOR"; //XOR
 *     public final static String GLOBAL_JOINTYPE_AND="AND"; //AND
 *
 *     public final static String GLOBAL_PERFORMMODE_MANUAL="Manual";//Manual
 *     public final static String GLOBAL_PERFORMMODE_MANUAL="Automatic";//Automatic
 *
 * Revision 1.2  2007/05/21 09:25:11  yuhong
 * MR#:NMBF30-9999 2007/05/21
 *
 * Revision 1.1  2007/05/17 06:22:07  qilin
 * no message
 *
 * Revision 1.7  2007/04/05 02:18:01  yuhong
 * MR#:NMBF30-9999 2007/04/05
 * add appmodule const
 *
 * Revision 1.6  2007/03/29 03:17:30  yuhong
 * MR#:NMBF30-9999 2007/03/29
 *
 * Revision 1.5  2007/03/20 08:08:02  yuhong
 * MR#:NMBF30-9999 2007/03/20
 * modify
 *
 * Revision 1.4  2007/03/15 03:06:37  yuhong
 * MR#:NMBF30-9999 2007/03/15
 *
 * Revision 1.3  2007/03/07 02:30:38  yuhong
 * MR#:NMBF30-9999 2007/03/07
 * ADD REGION_TYPE DIC
 *
 * Revision 1.2  2007/03/01 03:47:30  yuhong
 * MR#:NMBF30-9999 2007/03/01
 *
 * Revision 1.1  2007/02/27 07:20:22  yuhong
 * MR#:NMBF30-9999 2007/02/27
 * moved from afx
 *
 * Revision 1.57  2006/08/03 08:34:09  tailc
 * MR#:DN2B518-9999(代gaolu提交扩展属性的部分)
 *
 * Revision 1.56  2006/07/28 05:17:45  tailc
 * MR#:DN2B560-9999(为交换提交extendattribute的相关常量定义)
 *
 * Revision 1.55  2006/07/13 06:00:50  tailc
 * MR#:DN2B-9999(为dnpath.pathtype、dnbusinesspathaudits添加atm端口电路、ddn端口电路的相关常量)
 *
 * Revision 1.54  2006/04/13 06:24:19  xuqsh
 * MR#:NM60-0000
 *
 * Revision 1.53  2006/03/20 01:37:55  yuhong
 * MR#:NM60-9999
 *
 * Revision 1.52  2006/03/17 03:04:21  yuhong
 * MR#:NM60-9999
 *
 * Revision 1.51  2006/03/16 10:04:14  brookqi
 * MR#: NM60-2650
 * 添加常量AVAILABLECONFIGRATE_CATEGORY_CIRCUITPACKTYPE
 *
 * Revision 1.49  2006/01/25 02:33:10  yuhong
 * MR#:NM60-9999
 * add longOrLocal
 *
 * Revision 1.48  2005/11/22 02:08:41  yuhong
 * MR#:NM60-9999
 *
 * Revision 1.47  2005/11/21 05:35:48  yuhong
 * MR#:NM60-9999
 * add public static final byte PERSIST_TYPE_REMOVE_RELATION = 6;
 *
 * Revision 1.46  2005/08/16 08:51:04  yuhong
 * MR#:NM6XTASK-599
 *
 * Revision 1.45  2005/06/29 12:32:49  xuqsh
 * MR#:NM60-0000
 * 添加固定资产统计定时器
 *
 * Revision 1.44  2005/06/20 09:16:22  yuhong
 * MR#:NM60-9999
 *
 * Revision 1.43  2005/06/17 09:42:12  yuhong
 * MR#:NM60-9999
 * add const
 *
 * Revision 1.42  2005/06/17 09:03:46  yuhong
 * MR#:NM60-9999
 * add const
 *
 * Revision 1.41  2005/06/17 06:40:23  hxm
 * MR#:SN2-9999 add SITELINK_USESTATUS_
 *
 * Revision 1.40  2005/06/17 03:16:36  yuhong
 * MR#:DN2-2582
 * (可维护数据字典管理支持停用、启用功能)
 *
 * Revision 1.39  2005/06/14 09:58:33  yuhong
 * MR#:DN2-2574
 *
 * Revision 1.38  2005/06/14 07:42:44  hxm
 * MR#:SN2-9999 专业属性：add GLOBAL_SPECIALITY_
 *
 * Revision 1.37  2005/06/08 07:59:55  yuhong
 * MR#:DN2-9999
 *
 * Revision 1.36  2005/06/02 09:03:56  yuhong
 * MR#:NM60-9999
 * 固定资产审计字典
 *
 * Revision 1.35  2005/05/17 06:21:18  yuhong
 * MR#:NM60-9999
 * add appmodule
 *
 * Revision 1.34  2005/05/13 05:33:50  yuhong
 * MR#:NM60-9999
 * add 局内客户
 *
 * Revision 1.33  2005/04/27 03:05:08  yuhong
 * MR#:NM60-9999
 *
 * Revision 1.32  2005/04/22 01:18:10  yuhong
 * MR#:NM60-9999
 * "/" changeto ","
 *
 * Revision 1.31  2005/04/21 05:10:47  yuhong
 * MR#:NM60-9999
 * add global common division
 *
 * Revision 1.30  2005/04/19 01:48:53  panhui
 * MR#:DN2-9999
 *
 * Revision 1.29  2005/04/18 05:31:00  yuhong
 * MR#:NM60-9999
 *
 * Revision 1.28  2005/04/11 03:03:31  xuqsh
 * MR#:NM60-0000
 * 允许用户标识是否在删电路或割接完工时自动删除槽路
 *
 * Revision 1.27  2005/04/11 01:19:18  yuhong
 * MR#:DN2-2258
 * ADD EXTENDATTRIBUTE
 *
 * Revision 1.26  2005/03/17 12:34:10  hxm
 * MR#:NM60-9999 EXTENDATTRIBUTE_ENTITY_SNNE=9
 *
 * Revision 1.25  2005/03/17 08:53:42  yuhong
 * MR#:NM60-1920 传输扩展属性的实体
 *
 * Revision 1.24  2004/11/03 07:21:23  fengbin
 * MR#:NM60-0001
 * 添加AppModule
 * Serviceflow
 *
 * Revision 1.23  2004/09/29 08:39:51  yuhong
 * MR#:DN2-9999
 * customer add info
 *
 * Revision 1.22  2004/09/29 07:48:45  yuhong
 * MR#:DN2-9999
 * customer add info
 *
 * Revision 1.21  2004/09/29 04:52:15  yuhong
 * MR#: NM6XTASK-463
 *
 * Revision 1.20  2004/09/22 02:01:17  taienzhi
 * MR#:NM60-1253 权限参数配置缺少对新的数据字典管理方式的支持!
 *
 * Revision 1.19  2004/09/17 07:27:32  yuhong
 * MR#:NM60-9999
 *
 * Revision 1.18  2004/08/31 02:46:34  yuhong
 * MR#:DN2-9999
 * extendattribute moved from DN
 *
 * Revision 1.17  2004/08/19 09:22:14  yuhong
 * MR#:DN2-9999
 * 添加SYSDICTIONARYCATEGORY(subsystem,module,modifytype)
 *
 * Revision 1.16  2004/07/23 03:44:26  halcyon
 * MR#:DN2-00，rateList的相关控键移至afx！
 *
 * Revision 1.15  2004/07/23 02:19:07  qilin
 * MR#:NM60-0000
 * 数据网速率迁移
 *
 * Revision 1.14  2004/05/09 01:42:33  taienzhi
 * MR#:NM60-9999 Security add Organization
 *
 * Revision 1.13  2004/03/23 11:00:17  halcyon
 * MR#:NM50-8169，复制连接设备或连接面板时，端子名称没有复制。
 *
 * Revision 1.12  2004/03/23 06:47:10  miemie
 * MR#:NM60-0000
 *
 * Revision 1.11  2004/03/23 06:04:12  miemie
 * MR#:NM60-0000
 *
 * Revision 1.10  2003/08/12 02:03:26  taienzhi
 * add four attributes for CNC
 *
 * Revision 1.9  2003/07/29 02:59:14  yurenzhi
 * remove: public final static byte PERSIST_TYPE_COPY = 5;
 *
 * Revision 1.8  2003/07/29 02:31:50  yurenzhi
 * add: public final static byte PERSIST_TYPE_COPY = 5;
 *
 * Revision 1.7  2003/07/11 08:28:21  brookqi
 * add OPR_DELETE
 *
 * Revision 1.6  2003/06/03 05:41:07  brookqi
 * 把常量“OPR_” 设为final
 *
 * Revision 1.5  2002/12/30 01:33:08  lhl
 * add STRING_SEPARATOR2
 *
 * Revision 1.4  2002/12/20 07:03:07  lhl
 * add STRING_SEPARATOR for object.toString()
 *
 * Revision 1.3  2002/09/19 03:06:16  hfcao
 * 增加“操作类型”常量
 *
 * Revision 1.1  2002/02/05 07:55:29  wzwu
 * no message
 *
 ***************************************************************************/
package smartx.system.common.constant;

public class CommonSysConst {
    public final static String WILDCARD_SINGLE = "_";
    public final static String WILDCARD_MULTI = "%";

    public final static int LABELPOSITION_UP = 0;
    public final static int LABELPOSITION_DOWN = 1;
    public final static int LABELPOSITION_LEFT = 2;
    public final static int LABELPOSITION_RIGHT = 3;

    public final static int NULL_INT_FIELD = -2147483648;
    public final static byte NULL_BYTE_FIELD = -128;
    public final static int NULL_NUMERICAL_FIELD = -1;
    public final static byte NOT_NULL_FIELD = 99;

    public final static byte PERSIST_TYPE_NOCHANGE = 1;
    public final static byte PERSIST_TYPE_ADD = 2;
    public final static byte PERSIST_TYPE_UPDATE = 3;
    public final static byte PERSIST_TYPE_DELETE = 4;
    //add by halcyon for NM50-8169 on 2004/3/23(begin)
    public final static byte PERSIST_TYPE_COPY = 5;
    //add by halcyon for NM50-8169 on 2004/3/23(end)
    public static final byte PERSIST_TYPE_REMOVE_RELATION = 6;
    public static final byte PERSIST_TYPE_ADD_RELATION = 7;

    /* Operator */
    public final static int PUB_USER_ACCOUNTSTATUS_DISABLED = 0;
    public final static int PUB_USER_ACCOUNTSTATUS_ENABLED = 1;

    ///public final static int OPERATOR_CREATOR_SYSTEM                = 0;
    ///preDefined
    public final static int PUB_USER_PREDEFINED_NO = 0;
    public final static int PUB_USER_PREDEFINED_YES = 1;

    /* Role */
    ///preDefined
    public final static int PUB_ROLE_PREDEFINED_NO = 0;
    public final static int PUB_ROLE_PREDEFINED_YES = 1;

    /* Parameter */
    ///isSingle
    public final static int PUB_PARAMETER_ISSINGLE_YES = 1;
    public final static int PUB_PARAMETER_ISSINGLE_NO = 0;
    ///source
    public final static int PUB_PARAMETER_SOURCE_DICT = 0;
    public final static int PUB_PARAMETER_SOURCE_TABLE = 1;
    public final static int PUB_PARAMETER_SOURCE_MANUAL = 2;

    /* SecurityUnit */
    ///public final static int SECURITYUNIT_PREDEFINED_NO             = 0;
    ///public final static int SECURITYUNIT_PREDEFINED_YES            = 1;

    /* ParameterType */
    ///public final static int PARAMETERTYPE_ISSINGLE_YES             = 0;
    ///public final static int PARAMETERTYPE_ISSINGLE_NO              = 1;

    ///public final static int PARAMETERTYPE_SOURCE_DICT              = 0;
    ///public final static int PARAMETERTYPE_SOURCE_ER                = 1;
    ///public final static int PARAMETERTYPE_SOURCE_MANUAL            = 2;
    /*AppModule*/
    //add by Panfengxia 2001/07/30
    public final static String APPMODULE_NOVA = "NOVA";
    public final static String APPMODULE_BFBIZ = "BFBIZ";
    public final static String APPMODULE_BIZM = "BIZM";    //    营销支撑系统
    public final static String APPMODULE_DATMT = "DATMT";  //    数据维护系统


    //add end 2001/07/30


    //added by hfcao 2002/9/19
    public final static byte OPR_ADD = 0;
    public final static byte OPR_UPDATE = 1;
    public final static byte OPR_COPY = 2;
    public final static byte OPR_PROPERTY = 3;
    public final static byte OPR_DELETE = 4;

    //added by lhl 2002.12.20
    public static String STRING_SEPARATOR = "\u00B7";
    public static String STRING_SEPARATOR1 = "\u00A4";
    public static String STRING_SEPARATOR2 = "."; //for database store

    //add by tnz at 2003/08/12 for CNC
    public final static byte OPERATOR_SEX_MALE = 0; //男
    public final static byte OPERATOR_SEX_FEMALE = 1; //女

    public final static byte OPERATOR_WORKTYPE_NORMAL = 0; //常日班人员
    public final static byte OPERATOR_WORKTYPE_WATCH = 1; //值班人员

    public final static byte SYSDICTIONARY_TYPE_SYSTEM = 0; //系统
    public final static byte SYSDICTIONARY_TYPE_USER = 1; //用户

    public final static byte SYSDICTIONARY_STATE_UNUSED = 0; //停用
    public final static byte SYSDICTIONARY_STATE_USED = 1; //启用

    //add by yuhong 2004/08/19

    public final static byte SYSDICTIONARYCATEGORY_MODIFYTYPE_UNMAINTAIN = 0; //不可维护
    public final static byte SYSDICTIONARYCATEGORY_MODIFYTYPE_MAINTAIN = 1; //可维护

    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT1 = 1; //ext1
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT2 = 2; //ext2
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT3 = 3; //ext3
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT4 = 4; //ext4
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT5 = 5; //ext5
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT6 = 6; //ext6
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT7 = 7; //ext7
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT8 = 8; //ext8
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT9 = 9; //ext9
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT10 = 10; //ext10
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT11 = 11; //ext11
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT12 = 12; //ext12
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT13 = 13; //ext13
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT14 = 14; //ext14
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT15 = 15; //ext15
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT16 = 16; //ext16
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT17 = 17; //ext17
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT18 = 18; //ext18
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT19 = 19; //ext19
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT20 = 20; //ext20
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT21 = 21; //ext21
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT22 = 22; //ext22
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT23 = 23; //ext23
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT24 = 24; //ext24
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT25 = 25; //ext25
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT26 = 26; //ext26
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT27 = 27; //ext27
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT28 = 28; //ext28
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT29 = 29; //ext29
    public final static int EXTENDATTRIBUTE_ATTRIBUTENAME_EXT30 = 30; //ext30

    public final static int EXTENDATTRIBUTE_ENTITY_DEVICE = 1; // DNDevice
    public final static int EXTENDATTRIBUTE_ENTITY_PPORT = 2; // DNPort
    public final static int EXTENDATTRIBUTE_ENTITY_DNPACKAGE = 3; //DNPackage
    public final static int EXTENDATTRIBUTE_ENTITY_DNDEVICETYPE = 4; //DNDeviceType
    public final static int EXTENDATTRIBUTE_ENTITY_DNPACKAGETYPE = 5; //DNPackageType
    public final static int EXTENDATTRIBUTE_ENTITY_DNPath = 6; //DNPath
    public final static int EXTENDATTRIBUTE_ENTITY_ATM = 6; //ATM
    public final static int EXTENDATTRIBUTE_ENTITY_PROJET = 7; //PROJECT
    public final static int EXTENDATTRIBUTE_ENTITY_CUSTOMER = 8; //CUSTOMER
    public final static int EXTENDATTRIBUTE_ENTITY_SNNE = 9; //SNNE
    public final static int EXTENDATTRIBUTE_ENTITY_NE = 10; //NE
    public final static int EXTENDATTRIBUTE_ENTITY_CIRCUIT = 11; //CIRCUIT
    public final static int EXTENDATTRIBUTE_ENTITY_TANSPORTSYSTEM = 12; //TANSPORTSYSTEM
    public final static int EXTENDATTRIBUTE_ENTITY_TOPOSUBNETWORK = 13; //TOPOSUBNETWORK
    public final static int EXTENDATTRIBUTE_ENTITY_MANSERVICE = 14; //MANSERVICE
    public final static int EXTENDATTRIBUTE_ENTITY_DNVPNSERVICE = 15; //DNVPNSERVICE
    public final static int EXTENDATTRIBUTE_ENTITY_ASSET = 16; //ASSET
    public final static int EXTENDATTRIBUTE_ENTITY_COMMONSERVICE = 17; //COMMONSERVICE
    public final static int EXTENDATTRIBUTE_ENTITY_DDN = 18; //DDN
    public final static int EXTENDATTRIBUTE_ENTITY_FR = 19; //FR
    public final static int EXTENDATTRIBUTE_ENTITY_FRPORT = 20; //FRPORT
    public final static int EXTENDATTRIBUTE_ENTITY_DDNMULT = 21; //DDNMULT
    public final static int EXTENDATTRIBUTE_ENTITY_RELAYPATH = 22; //RELAYPATH
    public final static int EXTENDATTRIBUTE_ENTITY_USEPATH = 23; //USEPATH
    public final static int EXTENDATTRIBUTE_ENTITY_TWIGDEVICE = 24; //TWIGDEVICE
    //add by tailichun 20060713
    public final static int EXTENDATTRIBUTE_ENTITY_ATMPORT = 25; //ATMPORT
    public final static int EXTENDATTRIBUTE_ENTITY_DDNPORT = 26; //DDNPORT
    //add by tailichun ,for switchnetwork
    ////add by gaolu 20060728
    ////sn2-378-131 , 增加 SNTRUNK ,SNOLTONUTRUNK,SNINNERTRUNK , SN30BDTRUNK
    public final static int EXTENDATTRIBUTE_ENTITY_SNTRUNK = 27; //SNTRUNK
    public final static int EXTENDATTRIBUTE_ENTITY_SNOLTONUTRUNK = 28; //SNOLTONUT
    public final static int EXTENDATTRIBUTE_ENTITY_SNINNERTRUNK = 29; //SNINNERTRU
    public final static int EXTENDATTRIBUTE_ENTITY_SN30BDTRUNK = 30; //SN30BDTRUNK
    ////sn2-2090
    ////add by gaolu  20006/08/03
    public final static int EXTENDATTRIBUTE_ENTITY_SNV5INTERFACE = 31; //SNV5INTERFACE
    public final static int EXTENDATTRIBUTE_ENTITY_SNTRUNKGROUP = 32; //SNTRUNKGROUP
    public final static int EXTENDATTRIBUTE_ENTITY_SNSIGNALLINKSET = 33; //SNSIGNALLINKSET
    public final static int EXTENDATTRIBUTE_ENTITY_SNSIGNALLINK = 34; //SNSIGNALLINK
    public final static int EXTENDATTRIBUTE_ENTITY_SNBUSINESSCODE = 35; //SNBUSINESSCODE
    public final static int EXTENDATTRIBUTE_ENTITY_SNOFFICECODE = 36; //SNOFFICECODE

    public final static byte EXTENDATTRIBUTE_TYPE_STRING = 0; //字符串型
    public final static byte EXTENDATTRIBUTE_TYPE_NUMBER = 1; //数值型
    public final static byte EXTENDATTRIBUTE_TYPE_DICTIONARY = 2; //字典型
    public final static byte EXTENDATTRIBUTE_TYPE_DATE = 3; //日期型

    public final static byte CUSTOMER_GRADE_PLATINA = 1; //白金客户
    public final static byte CUSTOMER_GRADE_GOLD = 2; //黄金客户
    public final static byte CUSTOMER_GRADE_SAPPHIRE = 3; //蓝宝石客户
    public final static byte CUSTOMER_GRADE_RUBY = 4; //红宝石客户
    //add by jushi for 2009.06.02 begin
    public final static byte CUSTOMER_GRADE_NORMAL = 5; //普通
    public final static byte CUSTOMER_GRADE_SILVERCARD = 6; //银卡
    public final static byte CUSTOMER_GRADE_GOLDENCARD = 7; //金卡
    public final static byte CUSTOMER_GRADE_DIAMONDCARD = 8; //钻石卡
    public final static byte CUSTOMER_GRADE_MIDDLEHIGH = 9; //中高端
    public final static byte CUSTOMER_GRADE_HIGH = 10; //高端
    public final static byte CUSTOMER_GRADE_OVERTAKEHIGH = 11; //超高端

    public final static int CUSTOMER_CUSTSEX_MAN = 1;//男
    public final static int CUSTOMER_CUSTSEX_WOMAN = 2;//女
    //add by jushi for 2009.06.02 end
    
    public final static byte CUSTOMER_OPERATIONUNIT_NOTOPERATIONUNIT = 1; //非运营商
    public final static byte CUSTOMER_OPERATIONUNIT_MOBILE = 2; //中国移动
    public final static byte CUSTOMER_OPERATIONUNIT_CNC = 3; //中国网通
    public final static byte CUSTOMER_OPERATIONUNIT_UNION = 4; //中国联通
    public final static byte CUSTOMER_OPERATIONUNIT_TIETONG = 5; //中国铁通
    public final static byte CUSTOMER_OPERATIONUNIT_WEITONG = 6; //中国卫通
    public final static byte CUSTOMER_OPERATIONUNIT_TELECOM = 7; //中国电信

//moved from basic
    public final static byte CUSTOMER_ISHISTORY_NO = 0; //运营中
    public final static byte CUSTOMER_ISHISTORY_YES = 1; //注销

    public final static byte CUSTOMER_TYPE_BIG = 1; //大客户
    public final static byte CUSTOMER_TYPE_COMMERCE = 2; //商业客户
    public final static byte CUSTOMER_TYPE_PUGLIC = 3; //公众客户
    public final static byte CUSTOMER_TYPE_OFFICE = 4; //局内客户
    //add by jushi for 2009.06.02 begin
    public final static byte CUSTOMER_TYPE_UNKNOWN = 5; //未知
    public final static byte CUSTOMER_TYPE_NORMALCUST = 6; //普通客户
    public final static byte CUSTOMER_TYPE_GROUPCUST = 7; //集团客户
    public final static byte CUSTOMER_TYPE_CARDGROUPCUST = 8;    //卡式公话集团客户
    public final static byte CUSTOMER_TYPE_GOVCUST = 9; //政企客户
    public final static byte CUSTOMER_TYPE_CONUTRYINSTITUTION = 10; //国家机构
    public final static byte CUSTOMER_TYPE_CONUTRYARMING = 11; //国家武装力量
    public final static byte CUSTOMER_TYPE_GOVANDCASTE = 12; //政党机关及社会团体
    public final static byte CUSTOMER_TYPE_BANK = 13; //银行
    public final static byte CUSTOMER_TYPE_SECURITIES = 14; //证券

    public final static byte CUSTOMER_TYPE_INSURANCE = 15; //保险
    public final static byte CUSTOMER_TYPE_BIGCORTRAFFIC = 16; //大企业能源交通
    public final static byte CUSTOMER_TYPE_BIGCORMANUFACTURING = 17; //大企业制造业
    public final static byte CUSTOMER_TYPE_YYSANDISP = 18;    //运营商及ISP
    public final static byte CUSTOMER_TYPE_ELSEBIGCOR = 19; //其他大企业
    public final static byte CUSTOMER_TYPE_MIDSOURCETRAFF = 20; //中小企业能源交通
    public final static byte CUSTOMER_TYPE_MIDMANUFACTURING = 21; //中小企业制造业
    public final static byte CUSTOMER_TYPE_ELSESMALLCOR = 22; //其他种小企业
    public final static byte CUSTOMER_TYPE_TOURHOTEL = 23; //旅游、饭店及餐饮
    public final static byte CUSTOMER_TYPE_SCIENCEEDUCATIONAL = 24;    //科教
    public final static byte CUSTOMER_TYPE_HOSPITAL = 25; //医院
    public final static byte CUSTOMER_TYPE_WHOLESALERETAIL = 26; //批发零售
    public final static byte CUSTOMER_TYPE_NETBAR = 27; //网吧
    public final static byte CUSTOMER_TYPE_ELSE = 28; //其他聚类
    //add by jushi for 2009.06.02 end
    
//add by yuhong for common division
    public final static String GLOBAL_COMMON_DIVISION = ","; //分隔符
//add by yuhong 2005/04/27
    public final static byte GLOBAL_BOOLEAN_FALSE = 0;
    public final static byte GLOBAL_BOOLEAN_TRUE = 1;
    //add by yuhong 2007/02/27
    /**
     * 数字的初始值
     */
    public final static long NULL_LONG_FIELD = -2147483648;
    public final static float NOT_NULL_FLOAT = 0;
    public final static double NOT_NULL_DOUBLE = 0.0;

    /**
     * 持久化类型
     */
    public final static byte PERSIST_TYPE_REMOVE = 5;

    /**
     * 字符串分割符
     */
    public final static char String_Split_Char = ';';
    public final static String String_Split_String = ";";

    /** true */
    public final static String True = "true";
    public final static String DB_True = "1";

    /** false */
    public final static String False = "false";
    public final static String DB_False = "0"; //end add by yuhong 2007/02/27

    //add by yuhong 2007/03/07
    public final static int REGION_TYPE_BFTELECOM = -1; //北方电信
    public final static int REGION_TYPE_PROVINCE = 0; //省
    public final static int REGION_TYPE_CITY = 1; //地区，地级市，自治州
    public final static int REGION_TYPE_COUNTY = 2; //县，县级市，地级市的区
    public final static int REGION_TYPE_TOWN = 3; //乡，镇
    public final static int REGION_TYPE_VILLAGE = 4; //村
    //add by yuhong 2007/04/05
    public final static String GOBAL_APPMODULE_NOVA = "NOVA"; //NOVA平台
    public final static String GOBAL_APPMODULE_BFBIZ = "BFBIZ"; //服务开通
    public final static String GOBAL_APPMODULE_RESCONF = "RESCONF"; //资源配置
    

    //add by yuhong 2007/05/21
    public final static int PUB_TEMPLET_1_COMEFROM_SYS = 0; //系统
    public final static int PUB_TEMPLET_1_COMEFROM_USER = 1; //用户
    //add by yuhong 2007/05/30
    public final static String GLOBAL_JOINTYPE_XOR = "XOR"; //XOR
    public final static String GLOBAL_JOINTYPE_AND = "AND"; //AND

    public final static String GLOBAL_PERFORMMODE_MANUAL = "Manual"; //Manual
    public final static String GLOBAL_PERFORMMODE_AUTOMATIC = "Automatic"; //Automatic

    public final static String DATASOURCE_USERMGMT="datasource_usermgmt";
    
    
    //add by James.W 2007.09.03
    //TODO 加强系统变量管理，对于系统支持的信息，在此管理
    public final static String SYS_LOGINUSER_ID="SYS_LOGINUSER_ID";
    public final static String SYS_LOGINUSER_LOGINNAME="SYS_LOGINUSER_LOGINNAME";
    public final static String SYS_LOGINUSER_NAME="SYS_LOGINUSER_NAME";
    public final static String SYS_LOGIN_TIME="SYS_LOGIN_TIME";
    public final static String USER_LOGINCOUNT="USER_LOGINCOUNT";
    public final static String WORKPOSITION="WORKPOSITION";
    //add by James.W 2007.09.05 开放主界面标题栏
    public final static String SYS_LOGIN_USERINFO="SYS_LOGIN_USERINFO";
    //add by James.W 2007.09.06
    public final static String CLIENTINIT="CLIENTINIT";//Nova2Config.xml root/secondprojectclient
    public final static String CLIENTAFTERLOGIN="CLIENTAFTERLOGIN";//Nova2Config.xml root/secondprojectafterlogin
    public final static String CLIENTBEFORELOGOUT="CLIENTBEFORELOGOUT";//Nova2Config.xml root/secondprojectbeforelogout
    public final static String CLIENTPANELINIT="CLIENTPANELINIT";//Nova2Config.xml root/secondprojectpanelclient
    
    //add by jushi 2008.1.14
    public final static byte CUSTOMER_COMEFROM_OSS =0;//OSS
    public final static byte CUSTOMER_COMEFROM_BSS =1;//BSS
    public final static byte CUSTOMER_COMEFROM_CRM =2;//CRM
    
    //add by jushi 2008.03.06
    public final static byte CUSTOMER_CUSTSYSTYPE_BANDWITH =0;//带宽型
    public final static byte CUSTOMER_CUSTSYSTYPE_ACCESS =1;//接入型
    
    /** 元数据模板类型 */
	public static String MT_TYPE_DASHBOARD = "3";
	public static String MT_TYPE_PIECHART = "4";
	public static String MT_TYPE_ACCOUNTSET = "5";
	public static String MT_TYPE_ALERTINFO = "6";
	public static String MT_TYPE_COMBICHART = "7";
	public static String MT_TYPE_DISTRIBUTIONCHART = "8";
	public static String MT_TYPE_PIVOTCOMBINATIONCHART = "9";
	public static String MT_TYPE_GEOGRAPHYCHART = "10";
	public static String MT_TYPE_INDICATOR = "11";
	public static String MT_TYPE_TABLE = "12";
	public static String MT_TYPE_TREE_LIST = "13";
	public static String MT_TYPE_CARD = "14";
	public static String MT_TYPE_DATATASK = "20";
	public static String MT_TYPE_ANALYZERSET = "23";
	public static String MT_TYPE_SHORTCUTKEY = "101";
	public static String MT_TYPE_ENTITYMODEL = "103";
	public static String MT_TYPE_ENTITY_CMP = "108";
	public static String MT_TYPE_ENTITY_EXAMINE_TEMPLET = "109";
	public static String MT_TYPE_EXCEL_EXPORT_TEMPLET = "110";
	public static String MT_TYPE_GIS_APP_TEMPLET = "111";
}
