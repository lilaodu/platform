package com.blockchain.platform.enums;

/**
 * 验证重复提交验证
 */
public enum Timestamp {

    ONE_SECONDS(1L),TWO_SECONDS(2L),THREE_SECONDS(3L),FOUR_SECONDS(4L),FIVE_SECONDS(5L);

    private Long value;

    Timestamp(){}

    Timestamp(Long value){
        this.value = value;
    }

    public Long getValue() {
        return this.value;
    }
}
