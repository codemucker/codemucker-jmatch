/* -----------------------------------------------------------------------------
 * Displayer.java
 * -----------------------------------------------------------------------------
 *
 * Producer : com.parse2.aparse.Parser 2.5
 * Produced : Wed May 06 08:59:40 CST 2015
 *
 * -----------------------------------------------------------------------------
 */

package org.codemucker.jmatch.expression.parser;

import java.util.ArrayList;

public class Displayer implements Visitor
{

  public Object visit(Rule_MATCHERS rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_MATCHER rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_MTYPE rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_MEXPR rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_FILTERS rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_GROUP rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_AFILTERS rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_AFILTER rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_ATTNAME rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_ATTVALEXPR rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_ATTVAL rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_INTVAL rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_QVAL rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_IVAL rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_ANTEXPR rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_VAREXPR rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_VARNAME rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_QCHAR rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_GOPEN rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_GCLOSE rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_RANGE rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_BOOL rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_NULL rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_ALPHANUM rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_CHAR rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_VCHAR_MD rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_VCHAR_MS rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_VCHAR rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_ALPHA rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_DIGIT rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_LWSP rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_WSP rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_DQUOTE rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_SQUOTE rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_OR rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_AND rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_NOT rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_NOT_EQ rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_LESS rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_GREATER rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_LESS_EQ rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_GREATER_EQ rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_EQ rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_PLUS rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_NEG rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Terminal_StringValue value)
  {
    System.out.print(value.spelling);
    return null;
  }

  public Object visit(Terminal_NumericValue value)
  {
    System.out.print(value.spelling);
    return null;
  }

  private Object visitRules(ArrayList<Rule> rules)
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
