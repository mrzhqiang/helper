package helper.datetime;

import helper.DateTimeHelper;
import java.util.Date;

/**
 * 帖子。
 *
 * @author mrzhqiang
 */
public class Post {
  public static long nextId = 1;

  public long id;
  public String title;
  public String content;
  public long authorId;
  public Date created;
  public Date updated;

  public Post(String title, String content, long authorId, Date created, Date updated) {
    this.id = nextId++;
    this.title = title;
    this.content = content;
    this.authorId = authorId;
    this.created = created;
    this.updated = updated;
  }

  // other getter and setter method...

  public String showTime() {
    if (updated.after(created)) {
      return "更新于：" + DateTimeHelper.display(updated);
    }
    return "创建于：" + DateTimeHelper.display(created);
  }

  public static void main(String[] args) {
    Post post = new Post("震惊！台湾回归", "2020 年台湾当局决定回归大陆。", 1, new Date(), new Date());
    System.out.println(post.showTime());
  }
}
