/* -----------------------------------------------------------------------------
 * XmlDisplayer.java
 * -----------------------------------------------------------------------------
 *
 * Producer : com.parse2.aparse.Parser 2.5
 * Produced : Wed May 06 15:11:14 CST 2015
 *
 * -----------------------------------------------------------------------------
 */

package org.codemucker.jmatch.expression.parser;

import java.util.ArrayList;

public class XmlDisplayer implements Visitor
{
  private boolean terminal = true;

  public Object visit(Rule_matchers rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<matchers>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</matchers>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_matcher rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<matcher>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</matcher>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_mtype rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<mtype>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</mtype>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_mexpr rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<mexpr>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</mexpr>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_filter rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<filter>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</filter>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_group rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<group>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</group>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_afilters rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<afilters>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</afilters>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_afilter rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<afilter>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</afilter>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_attname rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<attname>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</attname>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_attvalexpr rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<attvalexpr>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</attvalexpr>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_attval rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<attval>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</attval>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_qval rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<qval>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</qval>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_ival rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<ival>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</ival>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_antexpr rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<antexpr>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</antexpr>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_varexpr rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<varexpr>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</varexpr>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_varname rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<varname>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</varname>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_range rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<range>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</range>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_rfrom rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<rfrom>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</rfrom>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_rto rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<rto>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</rto>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_datetime rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<datetime>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</datetime>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_date rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<date>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</date>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_time rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<time>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</time>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_tz rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<tz>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</tz>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_tzoffset rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<tzoffset>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</tzoffset>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_months rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<months>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</months>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_days rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<days>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</days>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_hours rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<hours>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</hours>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_minutes rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<minutes>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</minutes>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_seconds rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<seconds>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</seconds>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_gopen rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<gopen>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</gopen>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_gclose rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<gclose>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</gclose>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_NUM rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<NUM>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</NUM>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_FLOAT rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<FLOAT>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</FLOAT>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_LONG rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<LONG>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</LONG>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_DOUBLE rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<DOUBLE>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</DOUBLE>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_BINARY rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<BINARY>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</BINARY>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_HEX rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<HEX>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</HEX>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_SIGN rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<SIGN>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</SIGN>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_DQUOTE rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<DQUOTE>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</DQUOTE>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_SQUOTE rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<SQUOTE>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</SQUOTE>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_OR rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<OR>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</OR>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_AND rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<AND>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</AND>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_NOT rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<NOT>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</NOT>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_NOT_EQ rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<NOT_EQ>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</NOT_EQ>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_LESS rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<LESS>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</LESS>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_GREATER rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<GREATER>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</GREATER>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_LESS_EQ rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<LESS_EQ>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</LESS_EQ>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_GREATER_EQ rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<GREATER_EQ>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</GREATER_EQ>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_EQ rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<EQ>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</EQ>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_PLUS rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<PLUS>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</PLUS>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_NEG rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<NEG>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</NEG>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_BOOL rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<BOOL>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</BOOL>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_NULL rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<NULL>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</NULL>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_ALPHANUM rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<ALPHANUM>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</ALPHANUM>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_INT32 rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<INT32>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</INT32>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_CHAR rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<CHAR>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</CHAR>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_VCHAR rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<VCHAR>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</VCHAR>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_ALPHA rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<ALPHA>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</ALPHA>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_DIGIT rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<DIGIT>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</DIGIT>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_LWSP rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<LWSP>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</LWSP>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_WSP rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<WSP>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</WSP>");
    terminal = false;
    return null;
  }

  public Object visit(Terminal_StringValue value)
  {
    System.out.print(value.spelling);
    terminal = true;
    return null;
  }

  public Object visit(Terminal_NumericValue value)
  {
    System.out.print(value.spelling);
    terminal = true;
    return null;
  }

  private Boolean visitRules(ArrayList<Rule> rules)
  {
    for (Rule rule : rules)
      rule.accept(this);
    return null;
  }
}

/* -----------------------------------------------------------------------------
 * eof
 * -----------------------------------------------------------------------------
 */
