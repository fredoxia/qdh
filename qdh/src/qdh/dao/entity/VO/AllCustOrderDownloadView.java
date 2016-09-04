package qdh.dao.entity.VO;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

import qdh.utility.ExcelUtility;

public class AllCustOrderDownloadView  implements View {
	private String fileName = "所有客户订单";
    private final File dfile;
    private final String contentType = "application/octet-stream";
 
    public AllCustOrderDownloadView(String filePath) {
        this.dfile = new File(filePath);
    }
    
    public AllCustOrderDownloadView(String filePath, String fileName) {
        this.dfile = new File(filePath);
        this.fileName = fileName;
    }    
 
    @Override
    public String getContentType() {
        return this.contentType;
    }
 
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 避免下载时文件名乱码
        fileName = ExcelUtility.encodeExcelDownloadName(fileName + ".zip","KeHuBaoBiao.zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setContentType("application/octet-stream");
        response.setContentLength((int) dfile.length());
        // 得到输入流
        FileInputStream in = new FileInputStream(dfile);
        BufferedInputStream bufIn = new BufferedInputStream(in);
        ServletOutputStream out = response.getOutputStream();
        // 下面是一个普通的流的复制 。。。忽略 .这样可以防止内存问题
        byte[] bs = new byte[1024];
        long len = 0;
        while ((len = bufIn.read(bs)) != -1) {
            out.write(bs);
        }
        // 最后是流的关闭。
        out.flush();
        bufIn.close();
        in.close();
    }

}
