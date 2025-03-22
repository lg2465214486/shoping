package ltd.newbee.mall.controller.vo;

/**
 * @ClassName PayConfigVo
 * @Description
 * @Author luogan
 * @Date 2025/3/21 22:10
 */
public class PayConfigVo {

    private String usdtAddress;
    private String usdtImg;
    private String bankCard;
    private String bankImg;

    public String getUsdtAddress() {
        return usdtAddress;
    }

    public void setUsdtAddress(String usdtAddress) {
        this.usdtAddress = usdtAddress;
    }

    public String getUsdtImg() {
        return usdtImg;
    }

    public void setUsdtImg(String usdtImg) {
        this.usdtImg = usdtImg;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getBankImg() {
        return bankImg;
    }

    public void setBankImg(String bankImg) {
        this.bankImg = bankImg;
    }
}
