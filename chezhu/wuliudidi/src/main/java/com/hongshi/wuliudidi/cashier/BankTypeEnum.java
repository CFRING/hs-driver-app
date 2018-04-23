package com.hongshi.wuliudidi.cashier;

/**
 * Created on 2016/4/26.
 */
public enum BankTypeEnum {
    /**
     * 中国银行
     */
    bank_bocb(1, "BOCB2C", "中国银行"),
    /**
     * 工商银行
     */
    bank_icbc(2, "ICBCB2C", "工商银行"),
    /**
     * 招商银行
     */
    bank_cmb(3, "CMB", "招商银行"),
    /**
     * 建设银行
     */
    bank_ccb(4, "CCB", "建设银行"),
    /**
     * 农业银行
     */
    bank_abc(5, "ABC", "农业银行"),
    /**
     * 浦发银行
     */
    bank_spdb(6, "SPDB", "浦发银行"),
    /**
     * 兴业银行
     */
    bank_cib(7, "CIB", "兴业银行"),
    /**
     * 广发银行
     */
    bank_gdb(8, "GDB", "广发银行"),
    /**
     * 民生银行
     */
    bank_cmbc(9, "CMBC", "民生银行"),
    /**
     * 中信银行
     */
    bank_citic(10, "CITIC", "中信银行"),
    /**
     * 杭州银行
     */
    bank_hzcb(11, "HZCBB2C", "杭州银行"),
    /**
     * 光大银行
     */
    bank_cebbank(12, "CEB", "光大银行"),
    /**
     * 上海银行
     */
    bank_shbank(13, "SHBANK", "上海银行"),
    /**
     * 宁波银行
     */
    bank_nbbank(14, "NBBANK", "宁波银行"),
    /**
     * 平安银行
     */
    bank_spabank(15, "SPABANK", "平安银行"),
    /**
     * 交通银行
     */
    bank_comm(16, "COMM", "交通银行"),
    /**
     * 华夏银行
     */
    bank_hxbank(17, "HXBANK", "华夏银行"),
    /**
     * 邮储银行
     */
    bank_postgc(18, "POSTGC", "邮储银行");

    private final int    type;

    private final String bankCode;

    private final String bankName;

    private BankTypeEnum(int type, String bankCode, String bankName) {
        this.type = type;
        this.bankCode = bankCode;
        this.bankName = bankName;
    }

    /**
     * 通过type查找银行名字
     *
     * @param type
     * @return
     */
    public static String getBankName(int type) {
        for (BankTypeEnum tmp : BankTypeEnum.values()) {
            if (tmp.getType() == type) {
                return tmp.getBankName();
            }
        }
        return "";
    }

    /**
     * 通过type查找枚举
     *
     * @param type
     * @return
     */
    public static BankTypeEnum findByType(int type) {
        for (BankTypeEnum tmp : BankTypeEnum.values()) {
            if (tmp.getType() == type) {
                return tmp;
            }
        }
        return null;
    }

    /**
     * 通过code查找枚举
     *
     * @param code
     * @return
     */
    public static BankTypeEnum findByCode(String code) {
        for (BankTypeEnum tmp : BankTypeEnum.values()) {
            if (tmp.getBankCode().equals(code)) {
                return tmp;
            }
        }
        return null;
    }

    /**
     * 根据ID获取银行代码
     * @Title: getBankCode
     * @Description:
     * @param type
     * @return String
     * @throws
     */
    public static String getBankCode(int type) {
        for (BankTypeEnum tmp : BankTypeEnum.values()) {
            if (tmp.getType() == type) {
                return tmp.getBankCode();
            }
        }
        return "";
    }

    public int getType() {
        return type;
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getBankName() {
        return bankName;
    }
}