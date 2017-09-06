package com.at.frame;

import com.at.frame.plugin.Swagger2ApiDocumentationScanner;
import com.at.frame.utils.Result;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/6/23.
 */
public class Swagger2Servlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final List<String[]> groups = Swagger2ApiDocumentationScanner.GROUPS;
        StringBuilder builder = new StringBuilder("<html>" +
                "<head>" +
                    "<meta charset='utf-8'>" +
                    "<title>API列表</title>" +
                "</head><body>");
        builder.append("<div style = \"width:100%;height:100%\">");
        for(int i = 0, s = groups.size(); i < s; i++){
            String[] info = groups.get(i);
            builder.append("<div class=\"item\" style=\"border:1.5px solid #60affd;border-radius:3px;padding:12px;background:#ebf3fb;margin: 5px 0;cursor: pointer\">")
                    .append("<p style=\"color:#3b4375;margin: 0\">")
                    .append("<b style=\";margin-right:10px;\">")
                    .append("<a target=\"_blank\" href=\"swagger2/index.html?group=").append(info[0]).append("\">")
                    .append(info[0])
                    .append("</a>")
                    .append("</b>")
                    .append("&nbsp;&nbsp;")
                    .append(info[1])
                    .append("</p>")
                    .append("</div>");
        }
        builder.append("</body></html>");
        Result.write(resp,builder.toString());
    }
}
