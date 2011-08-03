/*
 * Copyright (c) 2005-2006, Luke Plant
 * All rights reserved.
 * E-mail: <L.Plant.98@cantab.net>
 * Web: http://lukeplant.me.uk/
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *
 *      * Redistributions in binary form must reproduce the above
 *        copyright notice, this list of conditions and the following
 *        disclaimer in the documentation and/or other materials provided
 *        with the distribution.
 *
 *      * The name of Luke Plant may not be used to endorse or promote
 *        products derived from this software without specific prior
 *        written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Rewrite with Java language and modified for lorsource by Ildar Hizbulin 2011
 * E-mail: <hizel@vyborg.ru>
 */

package ru.org.linux.util.bbcode.tags;

import ru.org.linux.site.User;
import ru.org.linux.site.UserNotFoundException;
import ru.org.linux.util.bbcode.Parser;
import ru.org.linux.util.bbcode.nodes.Node;
import ru.org.linux.util.bbcode.nodes.TextNode;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: hizel
 * Date: 7/6/11
 * Time: 12:27 AM
 */
public class MemberTag extends Tag {
  public MemberTag(String name, Set<String> allowedChildren, String implicitTag, Parser parser) {
    super(name, allowedChildren, implicitTag, parser);
  }

  @Override
  public String renderNodeXhtml(Node node, Connection db) {
    if (node.lengthChildren() == 0) {
      return "";
    }
    TextNode txtNode = (TextNode) node.getChildren().iterator().next();
    String memberName = Parser.escape(txtNode.getText()).trim();
    String pattern;
    try {
      User user = User.getUser(db, memberName);
      if (!user.isBlocked()) {
        pattern = "<span style=\"white-space: nowrap\"><img src=\"/img/tuxlor.png\"><a style=\"text-decoration: none\" href='/people/%s/profile'>%s</a></span>";
      } else {
        pattern = "<span style=\"white-space: nowrap\"><img src=\"/img/tuxlor.png\"><s><a style=\"text-decoration: none\" href='/people/%s/profile'>%s</a></s></span>";
      }
    } catch (UserNotFoundException ex) {
      pattern = "<s>%s</s>";
    } catch (SQLException e) {
      pattern = "<s>%s</s>"; // TODO throw this exception up
    }
    return String.format(pattern, Parser.escape(memberName), Parser.escape(memberName));

  }

  @Override
  public String renderNodeXhtml(Node node) {
    return renderNodeXhtml(node, null); // TODO там мы эксцепшн то перехватим? :-)
  }

}
