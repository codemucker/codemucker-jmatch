/* -----------------------------------------------------------------------------
 * XmlDisplayer.java
 * -----------------------------------------------------------------------------
 *
 * Producer : com.parse2.aparse.Parser 2.5
 * Produced : Wed May 06 08:59:40 CST 2015
 *
 * -----------------------------------------------------------------------------
 */

package org.codemucker.jmatch.expression.parser;

import java.util.ArrayList;

public class XmlDisplayer implements Visitor
{
  private boolean terminal = true;

  public Object visit(Rule_MATCHERS rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<MATCHERS>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</MATCHERS>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_MATCHER rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<MATCHER>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</MATCHER>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_MTYPE rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<MTYPE>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</MTYPE>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_MEXPR rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<MEXPR>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</MEXPR>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_FILTERS rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<FILTERS>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</FILTERS>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_GROUP rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<GROUP>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</GROUP>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_AFILTERS rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<AFILTERS>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</AFILTERS>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_AFILTER rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<AFILTER>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</AFILTER>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_ATTNAME rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<ATTNAME>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</ATTNAME>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_ATTVALEXPR rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<ATTVALEXPR>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</ATTVALEXPR>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_ATTVAL rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<ATTVAL>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</ATTVAL>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_INTVAL rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<INTVAL>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</INTVAL>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_QVAL rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<QVAL>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</QVAL>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_IVAL rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<IVAL>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</IVAL>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_ANTEXPR rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<ANTEXPR>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</ANTEXPR>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_VAREXPR rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<VAREXPR>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</VAREXPR>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_VARNAME rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<VARNAME>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</VARNAME>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_QCHAR rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<QCHAR>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</QCHAR>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_GOPEN rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<GOPEN>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</GOPEN>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_GCLOSE rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<GCLOSE>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</GCLOSE>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_RANGE rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<RANGE>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</RANGE>");
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

  public Object visit(Rule_VCHAR_MD rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<VCHAR_MD>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</VCHAR_MD>");
    terminal = false;
    return null;
  }

  public Object visit(Rule_VCHAR_MS rule)
  {
    if (!terminal) System.out.println();
    System.out.print("<VCHAR_MS>");
    terminal = false;
    visitRules(rule.rules);
    if (!terminal) System.out.println();
    System.out.print("</VCHAR_MS>");
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
