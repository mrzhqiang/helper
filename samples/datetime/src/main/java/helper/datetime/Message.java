package helper.datetime;

import com.google.common.base.MoreObjects;
import helper.DateTimeHelper;
import java.util.Date;

/**
 * @author mrzhqiang
 */
public final class Message {
  private static long nextId = 1;

  private long id;
  private String content;
  private Date timestamp;

  static Message ofText(String content) {
    Message message = new Message();
    message.content = content;
    message.timestamp = new Date();
    return message;
  }

  private Message() {
    id = nextId++;
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("content", content)
        .add("timestamp", DateTimeHelper.display(timestamp))
        .toString();
  }
}
