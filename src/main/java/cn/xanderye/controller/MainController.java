package cn.xanderye.controller;

import cn.xanderye.service.GenerateService;
import cn.xanderye.util.JavaFxUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author XanderYe
 * @date 2020/2/6
 */
@Slf4j
public class MainController implements Initializable {
    @FXML
    private TextField groupIdText, artifactIdText, authorText, dateText, nameText;
    @FXML
    private CheckBox baseCheckBox;
    @FXML
    private Button generateBtn;

    private GenerateService generateService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateService = new GenerateService();
    }

    public void startGenerate() {
        String groupId = groupIdText.getText();
        String artifactId = artifactIdText.getText();
        String author = authorText.getText();
        String date = dateText.getText();
        String name = nameText.getText();
        if (StringUtils.isAnyEmpty(groupId, author, date, name)) {
            JavaFxUtil.errorDialog("警告", "请填写必填参数（除artifactId外）");
            return;
        }
        generateBtn.setDisable(true);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            log.info("groupId: {}, artifactId: {}, 作者: {}, 日期: {}", groupId, artifactId, author, date);
            String targetPath = System.getProperty("user.dir");
            log.info("生成路径：{}", targetPath);
            try {
                if (baseCheckBox.isSelected()) {
                    log.info("勾选了base包，生成base包");
                    generateService.generateBase(groupId, artifactId, author, date, targetPath);
                    baseCheckBox.setSelected(false);
                }
                log.info("生成{}Mapper和{}Service", name, name);
                generateService.generateService(groupId, artifactId, author, date, name, targetPath);
                Platform.runLater(() -> {
                    generateBtn.setDisable(false);
                    JavaFxUtil.alertDialog("成功", "生成成功");
                });
            } catch (Exception e) {
                log.error("生成失败，原因：{}", e.getMessage());
                Platform.runLater(() -> JavaFxUtil.errorDialog("错误", "生成失败"));
            }
        });
        executorService.shutdown();
    }
}
