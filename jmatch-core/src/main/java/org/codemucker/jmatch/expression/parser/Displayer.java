/* -----------------------------------------------------------------------------
 * Displayer.java
 * -----------------------------------------------------------------------------
 *
 * Producer : com.parse2.aparse.Parser 2.5
 * Produced : Wed May 06 15:11:14 CST 2015
 *
 * -----------------------------------------------------------------------------
 */

package org.codemucker.jmatch.expression.parser;

import java.util.ArrayList;

public class Displayer implements Visitor
{

  public Object visit(Rule_matchers rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_matcher rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_mtype rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_mexpr rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_filter rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_group rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_afilters rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_afilter rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_attname rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_attvalexpr rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_attval rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_qval rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_ival rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_antexpr rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_varexpr rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_varname rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_range rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_rfrom rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_rto rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_datetime rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_date rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_time rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_tz rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_tzoffset rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_months rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_days rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_hours rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_minutes rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_seconds rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_gopen rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_gclose rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_NUM rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_FLOAT rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_LONG rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_DOUBLE rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_BINARY rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_HEX rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_SIGN rule)
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

  public Object visit(Rule_INT32 rule)
  {
    return visitRules(rule.rules);
  }

  public Object visit(Rule_CHAR rule)
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
