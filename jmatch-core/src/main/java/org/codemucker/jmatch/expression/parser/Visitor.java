/* -----------------------------------------------------------------------------
 * Visitor.java
 * -----------------------------------------------------------------------------
 *
 * Producer : com.parse2.aparse.Parser 2.5
 * Produced : Wed May 06 15:11:14 CST 2015
 *
 * -----------------------------------------------------------------------------
 */

package org.codemucker.jmatch.expression.parser;

public interface Visitor
{
  public Object visit(Rule_matchers rule);
  public Object visit(Rule_matcher rule);
  public Object visit(Rule_mtype rule);
  public Object visit(Rule_mexpr rule);
  public Object visit(Rule_filter rule);
  public Object visit(Rule_group rule);
  public Object visit(Rule_afilters rule);
  public Object visit(Rule_afilter rule);
  public Object visit(Rule_attname rule);
  public Object visit(Rule_attvalexpr rule);
  public Object visit(Rule_attval rule);
  public Object visit(Rule_qval rule);
  public Object visit(Rule_ival rule);
  public Object visit(Rule_antexpr rule);
  public Object visit(Rule_varexpr rule);
  public Object visit(Rule_varname rule);
  public Object visit(Rule_range rule);
  public Object visit(Rule_rfrom rule);
  public Object visit(Rule_rto rule);
  public Object visit(Rule_datetime rule);
  public Object visit(Rule_date rule);
  public Object visit(Rule_time rule);
  public Object visit(Rule_tz rule);
  public Object visit(Rule_tzoffset rule);
  public Object visit(Rule_months rule);
  public Object visit(Rule_days rule);
  public Object visit(Rule_hours rule);
  public Object visit(Rule_minutes rule);
  public Object visit(Rule_seconds rule);
  public Object visit(Rule_gopen rule);
  public Object visit(Rule_gclose rule);
  public Object visit(Rule_NUM rule);
  public Object visit(Rule_FLOAT rule);
  public Object visit(Rule_LONG rule);
  public Object visit(Rule_DOUBLE rule);
  public Object visit(Rule_BINARY rule);
  public Object visit(Rule_HEX rule);
  public Object visit(Rule_SIGN rule);
  public Object visit(Rule_DQUOTE rule);
  public Object visit(Rule_SQUOTE rule);
  public Object visit(Rule_OR rule);
  public Object visit(Rule_AND rule);
  public Object visit(Rule_NOT rule);
  public Object visit(Rule_NOT_EQ rule);
  public Object visit(Rule_LESS rule);
  public Object visit(Rule_GREATER rule);
  public Object visit(Rule_LESS_EQ rule);
  public Object visit(Rule_GREATER_EQ rule);
  public Object visit(Rule_EQ rule);
  public Object visit(Rule_PLUS rule);
  public Object visit(Rule_NEG rule);
  public Object visit(Rule_BOOL rule);
  public Object visit(Rule_NULL rule);
  public Object visit(Rule_ALPHANUM rule);
  public Object visit(Rule_INT32 rule);
  public Object visit(Rule_CHAR rule);
  public Object visit(Rule_VCHAR rule);
  public Object visit(Rule_ALPHA rule);
  public Object visit(Rule_DIGIT rule);
  public Object visit(Rule_LWSP rule);
  public Object visit(Rule_WSP rule);

  public Object visit(Terminal_StringValue value);
  public Object visit(Terminal_NumericValue value);
}

/* -----------------------------------------------------------------------------
 * eof
 * -----------------------------------------------------------------------------
 */
