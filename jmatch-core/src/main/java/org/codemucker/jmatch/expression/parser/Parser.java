/* -----------------------------------------------------------------------------
 * Parser.java
 * -----------------------------------------------------------------------------
 *
 * Producer : com.parse2.aparse.Parser 2.5
 * Produced : Mon May 04 20:57:29 CST 2015
 *
 * -----------------------------------------------------------------------------
 */

package org.codemucker.jmatch.expression.parser;

import java.util.Stack;
import java.util.Properties;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;

public class Parser
{
  private Parser() {}

  static public void main(String[] args)
  {
    Properties arguments = new Properties();
    String error = "";
    boolean ok = args.length > 0;

    if (ok)
    {
      arguments.setProperty("Trace", "Off");
      arguments.setProperty("Rule", "MATCHERS");

      for (int i = 0; i < args.length; i++)
      {
        if (args[i].equals("-trace"))
          arguments.setProperty("Trace", "On");
        else if (args[i].equals("-visitor"))
          arguments.setProperty("Visitor", args[++i]);
        else if (args[i].equals("-file"))
          arguments.setProperty("File", args[++i]);
        else if (args[i].equals("-string"))
          arguments.setProperty("String", args[++i]);
        else if (args[i].equals("-rule"))
          arguments.setProperty("Rule", args[++i]);
        else
        {
          error = "unknown argument: " + args[i];
          ok = false;
        }
      }
    }

    if (ok)
    {
      if (arguments.getProperty("File") == null &&
          arguments.getProperty("String") == null)
      {
        error = "insufficient arguments: -file or -string required";
        ok = false;
      }
    }

    if (!ok)
    {
      System.out.println("error: " + error);
      System.out.println("usage: Parser [-rule rulename] [-trace] <-file file | -string string> [-visitor visitor]");
    }
    else
    {
      try
      {
        Rule rule = null;

        if (arguments.getProperty("File") != null)
        {
          rule = 
            parse(
              arguments.getProperty("Rule"), 
              new File(arguments.getProperty("File")), 
              arguments.getProperty("Trace").equals("On"));
        }
        else if (arguments.getProperty("String") != null)
        {
          rule = 
            parse(
              arguments.getProperty("Rule"), 
              arguments.getProperty("String"), 
              arguments.getProperty("Trace").equals("On"));
        }

        if (arguments.getProperty("Visitor") != null)
        {
          Visitor visitor = 
            (Visitor)Class.forName(arguments.getProperty("Visitor")).newInstance();
          rule.accept(visitor);
        }
      }
      catch (IllegalArgumentException e)
      {
        System.out.println("argument error: " + e.getMessage());
      }
      catch (IOException e)
      {
        System.out.println("io error: " + e.getMessage());
      }
      catch (ParserException e)
      {
        System.out.println("parser error: " + e.getMessage());
      }
      catch (ClassNotFoundException e)
      {
        System.out.println("visitor error: class not found - " + e.getMessage());
      }
      catch (IllegalAccessException e)
      {
        System.out.println("visitor error: illegal access - " + e.getMessage());
      }
      catch (InstantiationException e)
      {
        System.out.println("visitor error: instantiation failure - " + e.getMessage());
      }
    }
  }

  static public Rule parse(String rulename, String string)
  throws IllegalArgumentException,
         ParserException
  {
    return parse(rulename, string, false);
  }

  static public Rule parse(String rulename, InputStream in)
  throws IllegalArgumentException,
         IOException,
         ParserException
  {
    return parse(rulename, in, false);
  }

  static public Rule parse(String rulename, File file)
  throws IllegalArgumentException,
         IOException,
         ParserException
  {
    return parse(rulename, file, false);
  }

  static private Rule parse(String rulename, String string, boolean trace)
  throws IllegalArgumentException,
         ParserException
  {
    if (rulename == null)
      throw new IllegalArgumentException("null rulename");
    if (string == null)
      throw new IllegalArgumentException("null string");

    ParserContext context = new ParserContext(string, trace);

    Rule rule = null;
    if (rulename.equalsIgnoreCase("MATCHERS")) rule = Rule_MATCHERS.parse(context);
    else if (rulename.equalsIgnoreCase("MATCHER")) rule = Rule_MATCHER.parse(context);
    else if (rulename.equalsIgnoreCase("MTYPE")) rule = Rule_MTYPE.parse(context);
    else if (rulename.equalsIgnoreCase("EXPR")) rule = Rule_EXPR.parse(context);
    else if (rulename.equalsIgnoreCase("GFILTERS")) rule = Rule_GFILTERS.parse(context);
    else if (rulename.equalsIgnoreCase("FILTERS")) rule = Rule_FILTERS.parse(context);
    else if (rulename.equalsIgnoreCase("FILTER")) rule = Rule_FILTER.parse(context);
    else if (rulename.equalsIgnoreCase("ATTNAME")) rule = Rule_ATTNAME.parse(context);
    else if (rulename.equalsIgnoreCase("ATTVALEXPR")) rule = Rule_ATTVALEXPR.parse(context);
    else if (rulename.equalsIgnoreCase("ATTVAL")) rule = Rule_ATTVAL.parse(context);
    else if (rulename.equalsIgnoreCase("INTVAL")) rule = Rule_INTVAL.parse(context);
    else if (rulename.equalsIgnoreCase("QVAL")) rule = Rule_QVAL.parse(context);
    else if (rulename.equalsIgnoreCase("IVAL")) rule = Rule_IVAL.parse(context);
    else if (rulename.equalsIgnoreCase("ANTEXPR")) rule = Rule_ANTEXPR.parse(context);
    else if (rulename.equalsIgnoreCase("VAREXPR")) rule = Rule_VAREXPR.parse(context);
    else if (rulename.equalsIgnoreCase("VARNAME")) rule = Rule_VARNAME.parse(context);
    else if (rulename.equalsIgnoreCase("QCHAR")) rule = Rule_QCHAR.parse(context);
    else if (rulename.equalsIgnoreCase("GOPEN")) rule = Rule_GOPEN.parse(context);
    else if (rulename.equalsIgnoreCase("GCLOSE")) rule = Rule_GCLOSE.parse(context);
    else if (rulename.equalsIgnoreCase("RANGE")) rule = Rule_RANGE.parse(context);
    else if (rulename.equalsIgnoreCase("BOOL")) rule = Rule_BOOL.parse(context);
    else if (rulename.equalsIgnoreCase("NULL")) rule = Rule_NULL.parse(context);
    else if (rulename.equalsIgnoreCase("ALPHANUM")) rule = Rule_ALPHANUM.parse(context);
    else if (rulename.equalsIgnoreCase("CHAR")) rule = Rule_CHAR.parse(context);
    else if (rulename.equalsIgnoreCase("VCHAR_MD")) rule = Rule_VCHAR_MD.parse(context);
    else if (rulename.equalsIgnoreCase("VCHAR_MS")) rule = Rule_VCHAR_MS.parse(context);
    else if (rulename.equalsIgnoreCase("VCHAR")) rule = Rule_VCHAR.parse(context);
    else if (rulename.equalsIgnoreCase("ALPHA")) rule = Rule_ALPHA.parse(context);
    else if (rulename.equalsIgnoreCase("DIGIT")) rule = Rule_DIGIT.parse(context);
    else if (rulename.equalsIgnoreCase("LWSP")) rule = Rule_LWSP.parse(context);
    else if (rulename.equalsIgnoreCase("WSP")) rule = Rule_WSP.parse(context);
    else if (rulename.equalsIgnoreCase("DQUOTE")) rule = Rule_DQUOTE.parse(context);
    else if (rulename.equalsIgnoreCase("SQUOTE")) rule = Rule_SQUOTE.parse(context);
    else if (rulename.equalsIgnoreCase("OR")) rule = Rule_OR.parse(context);
    else if (rulename.equalsIgnoreCase("AND")) rule = Rule_AND.parse(context);
    else if (rulename.equalsIgnoreCase("NOT")) rule = Rule_NOT.parse(context);
    else if (rulename.equalsIgnoreCase("NOT_EQ")) rule = Rule_NOT_EQ.parse(context);
    else if (rulename.equalsIgnoreCase("LESS")) rule = Rule_LESS.parse(context);
    else if (rulename.equalsIgnoreCase("GREATER")) rule = Rule_GREATER.parse(context);
    else if (rulename.equalsIgnoreCase("LESS_EQ")) rule = Rule_LESS_EQ.parse(context);
    else if (rulename.equalsIgnoreCase("GREATER_EQ")) rule = Rule_GREATER_EQ.parse(context);
    else if (rulename.equalsIgnoreCase("EQ")) rule = Rule_EQ.parse(context);
    else if (rulename.equalsIgnoreCase("PLUS")) rule = Rule_PLUS.parse(context);
    else if (rulename.equalsIgnoreCase("NEG")) rule = Rule_NEG.parse(context);
    else throw new IllegalArgumentException("unknown rule");

    if (rule == null)
    {
      throw new ParserException(
        "rule \"" + (String)context.getErrorStack().peek() + "\" failed",
        context.text,
        context.getErrorIndex(),
        context.getErrorStack());
    }

    if (context.text.length() > context.index)
    {
      ParserException primaryError = 
        new ParserException(
          "extra data found",
          context.text,
          context.index,
          new Stack<String>());

      if (context.getErrorIndex() > context.index)
      {
        ParserException secondaryError = 
          new ParserException(
            "rule \"" + (String)context.getErrorStack().peek() + "\" failed",
            context.text,
            context.getErrorIndex(),
            context.getErrorStack());

        primaryError.initCause(secondaryError);
      }

      throw primaryError;
    }

    return rule;
  }

  static private Rule parse(String rulename, InputStream in, boolean trace)
  throws IllegalArgumentException,
         IOException,
         ParserException
  {
    if (rulename == null)
      throw new IllegalArgumentException("null rulename");
    if (in == null)
      throw new IllegalArgumentException("null input stream");

    int ch = 0;
    StringBuffer out = new StringBuffer();
    while ((ch = in.read()) != -1)
      out.append((char)ch);

    return parse(rulename, out.toString(), trace);
  }

  static private Rule parse(String rulename, File file, boolean trace)
  throws IllegalArgumentException,
         IOException,
         ParserException
  {
    if (rulename == null)
      throw new IllegalArgumentException("null rulename");
    if (file == null)
      throw new IllegalArgumentException("null file");

    BufferedReader in = new BufferedReader(new FileReader(file));
    int ch = 0;
    StringBuffer out = new StringBuffer();
    while ((ch = in.read()) != -1)
      out.append((char)ch);

    in.close();

    return parse(rulename, out.toString(), trace);
  }
}

/* -----------------------------------------------------------------------------
 * eof
 * -----------------------------------------------------------------------------
 */
