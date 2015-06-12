package com.kerkr.edu.utill;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * ShellUtils
 * <ul>
 * <strong>Check root</strong>
 * <li>{@link ShellUtils#checkRootPermission()}</li>
 * </ul>
 * <ul>
 * <strong>Execte command</strong>
 * <li>{@link ShellUtils#execCommand(String, boolean)}</li>
 * <li>{@link ShellUtils#execCommand(String, boolean, boolean)}</li>
 * <li>{@link ShellUtils#execCommand(List, boolean)}</li>
 * <li>{@link ShellUtils#execCommand(List, boolean, boolean)}</li>
 * <li>{@link ShellUtils#execCommand(String[], boolean)}</li>
 * <li>{@link ShellUtils#execCommand(String[], boolean, boolean)}</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-16
 */
public class ShellUtils {

    public static final String COMMAND_SU       = "su";
    public static final String COMMAND_SH       = "sh";
    public static final String COMMAND_EXIT     = "exit\n";
    public static final String COMMAND_LINE_END = "\n";

    private ShellUtils() {
        throw new AssertionError();
    }

    /**
     * check whether has root permission
     * 
     * @return
     */
    public static boolean checkRootPermission() {
        return execCommand("echo root", true, false).result == 0;
    }

    /**
     * execute shell command, default return result msg
     * 
     * @param command command
     * @param isRoot whether need to run with root
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(String command, boolean isRoot) {
        return execCommand(new String[] {command}, isRoot, true);
    }

    /**
     * execute shell commands, default return result msg
     * 
     * @param commands command list
     * @param isRoot whether need to run with root
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(List<String> commands, boolean isRoot) {
        return execCommand(commands == null ? null : commands.toArray(new String[] {}), isRoot, true);
    }

    /**
     * execute shell commands, default return result msg
     * 
     * @param commands command array
     * @param isRoot whether need to run with root
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot) {
        return execCommand(commands, isRoot, true);
    }

    /**
     * execute shell command
     * 
     * @param command command
     * @param isRoot whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(String command, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(new String[] {command}, isRoot, isNeedResultMsg);
    }

    /**
     * execute shell commands
     * 
     * @param commands command list
     * @param isRoot whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return
     * @see ShellUtils#execCommand(String[], boolean, boolean)
     */
    public static CommandResult execCommand(List<String> commands, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(commands == null ? null : commands.toArray(new String[] {}), isRoot, isNeedResultMsg);
    }

    /**
     * execute shell commands
     * 
     * @param commands command array
     * @param isRoot whether need to run with root
     * @param isNeedResultMsg whether need result msg
     * @return <ul>
     *         <li>if isNeedResultMsg is false, {@link CommandResult#successMsg} is null and
     *         {@link CommandResult#errorMsg} is null.</li>
     *         <li>if {@link CommandResult#result} is -1, there maybe some excepiton.</li>
     *         </ul>
     */
    public static CommandResult execCommand(String[] commands, boolean isRoot, boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }

        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;

        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }

                // donnot use os.writeBytes(commmand), avoid chinese charset error
                os.write(command.getBytes());
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            result = process.waitFor();
            // get command result
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String s;
                while ((s = successResult.readLine()) != null) {
                    successMsg.append(s);
                }
                while ((s = errorResult.readLine()) != null) {
                    errorMsg.append(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (process != null) {
                process.destroy();
            }
        }
        return new CommandResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null
                : errorMsg.toString());
    }

    /**
     * result of command
     * <ul>
     * <li>{@link CommandResult#result} means result of command, 0 means normal, else means error, same to excute in
     * linux shell</li>
     * <li>{@link CommandResult#successMsg} means success message of command result</li>
     * <li>{@link CommandResult#errorMsg} means error message of command result</li>
     * </ul>
     * 
     * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-16
     */
    public static class CommandResult {

        /** result of command **/
        public int    result;
        /** success message of command result **/
        public String successMsg;
        /** error message of command result **/
        public String errorMsg;

        public CommandResult(int result) {
            this.result = result;
        }

        public CommandResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }
    
    

    /** Return the first line of /proc/stat or null if failed. */
    public String readSystemStat() {

        RandomAccessFile reader = null;
        String load = null;

        try {
            reader = new RandomAccessFile("/proc/stat", "r");
            load = reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Streams.close(reader);
        }

        return load;
    }

    /**
     * Compute and return the total CPU usage, in percent.
     * 
     * @param start
     *            first content of /proc/stat. Not null.
     * @param end
     *            second content of /proc/stat. Not null.
     * @return the CPU use in percent, or -1f if the stats are inverted or on
     *         error
     * @see {@link #readSystemStat()}
     */
    public float getSystemCpuUsage(String start, String end) {
        String[] stat = start.split(" ");
        long idle1 = getSystemIdleTime(stat);
        long up1 = getSystemUptime(stat);

        stat = end.split(" ");
        long idle2 = getSystemIdleTime(stat);
        long up2 = getSystemUptime(stat);

        // don't know how it is possible but we should care about zero and
        // negative values.
        float cpu = -1f;
        if (idle1 >= 0 && up1 >= 0 && idle2 >= 0 && up2 >= 0) {
            if ((up2 + idle2) > (up1 + idle1) && up2 >= up1) {
                cpu = (up2 - up1) / (float) ((up2 + idle2) - (up1 + idle1));
                cpu *= 100.0f;
            }
        }

        return cpu;
    }

    /**
     * Return the sum of uptimes read from /proc/stat.
     * 
     * @param stat
     *            see {@link #readSystemStat()}
     */
    public long getSystemUptime(String[] stat) {
        /*
         * (from man/5/proc) /proc/stat kernel/system statistics. Varies with
         * architecture. Common entries include: cpu 3357 0 4313 1362393
         * 
         * The amount of time, measured in units of USER_HZ (1/100ths of a
         * second on most architectures, use sysconf(_SC_CLK_TCK) to obtain the
         * right value), that the system spent in user mode, user mode with low
         * priority (nice), system mode, and the idle task, respectively. The
         * last value should be USER_HZ times the second entry in the uptime
         * pseudo-file.
         * 
         * In Linux 2.6 this line includes three additional columns: iowait -
         * time waiting for I/O to complete (since 2.5.41); irq - time servicing
         * interrupts (since 2.6.0-test4); softirq - time servicing softirqs
         * (since 2.6.0-test4).
         * 
         * Since Linux 2.6.11, there is an eighth column, steal - stolen time,
         * which is the time spent in other operating systems when running in a
         * virtualized environment
         * 
         * Since Linux 2.6.24, there is a ninth column, guest, which is the time
         * spent running a virtual CPU for guest operating systems under the
         * control of the Linux kernel.
         */

        // with the following algorithm, we should cope with all versions and
        // probably new ones.
        long l = 0L;
        for (int i = 2; i < stat.length; i++) {
            if (i != 5) { // bypass any idle mode. There is currently only one.
                try {
                    l += Long.parseLong(stat[i]);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    return -1L;
                }
            }
        }

        return l;
    }

    /**
     * Return the sum of idle times read from /proc/stat.
     * 
     * @param stat
     *            see {@link #readSystemStat()}
     */
    public long getSystemIdleTime(String[] stat) {
        try {
            return Long.parseLong(stat[5]);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        return -1L;
    }

    /** Return the first line of /proc/pid/stat or null if failed. */
    public String readProcessStat(int pid) {

        RandomAccessFile reader = null;
        String line = null;

        try {
            reader = new RandomAccessFile("/proc/" + pid + "/stat", "r");
            line = reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Streams.close(reader);
        }

        return line;
    }

    /**
     * Compute and return the CPU usage for a process, in percent.
     * 
     * <p>
     * The parameters {@code totalCpuTime} is to be the one for the same period
     * of time delimited by {@code statStart} and {@code statEnd}.
     * </p>
     * 
     * @param start
     *            first content of /proc/pid/stat. Not null.
     * @param end
     *            second content of /proc/pid/stat. Not null.
     * @return the CPU use in percent or -1f if the stats are inverted or on
     *         error
     * @param uptime
     *            sum of user and kernel times for the entire system for the
     *            same period of time.
     * @return 12.7 for a cpu usage of 12.7% or -1 if the value is not available
     *         or an error occurred.
     * @see {@link #readProcessStat(int)}
     */
    public float getProcessCpuUsage(String start, String end, long uptime) {

        String[] stat = start.split(" ");
        long up1 = getProcessUptime(stat);

        stat = end.split(" ");
        long up2 = getProcessUptime(stat);

        float ret = -1f;
        if (up1 >= 0 && up2 >= up1 && uptime > 0.) {
            ret = 100.f * (up2 - up1) / (float) uptime;
        }

        return ret;
    }

    /**
     * Decode the fields of the file {@code /proc/pid/stat} and return (utime +
     * stime)
     * 
     * @param stat
     *            obtained with {@link #readProcessStat(int)}
     */
    public long getProcessUptime(String[] stat) {
        return Long.parseLong(stat[14]) + Long.parseLong(stat[15]);
    }

    /**
     * Decode the fields of the file {@code /proc/pid/stat} and return (cutime +
     * cstime)
     * 
     * @param stat
     *            obtained with {@link #readProcessStat(int)}
     */
    public long getProcessIdleTime(String[] stat) {
        return Long.parseLong(stat[16]) + Long.parseLong(stat[17]);
    }

    /**
     * Return the total CPU usage, in percent.
     * <p>
     * The call is blocking for the time specified by elapse.
     * </p>
     * 
     * @param elapse
     *            the time in milliseconds between reads.
     * @return 12.7 for a CPU usage of 12.7% or -1 if the value is not
     *         available.
     */
    public float syncGetSystemCpuUsage(long elapse) {

        String stat1 = readSystemStat();
        if (stat1 == null) {
            return -1.f;
        }

        try {
            Thread.sleep(elapse);
        } catch (Exception e) {
        }

        String stat2 = readSystemStat();
        if (stat2 == null) {
            return -1.f;
        }

        return getSystemCpuUsage(stat1, stat2);
    }

    /**
     * Return the CPU usage of a process, in percent.
     * <p>
     * The call is blocking for the time specified by elapse.
     * </p>
     * 
     * @param pid
     * @param elapse
     *            the time in milliseconds between reads.
     * @return 6.32 for a CPU usage of 6.32% or -1 if the value is not
     *         available.
     */
    public float syncGetProcessCpuUsage(int pid) {

        String pidStat1 = readProcessStat(pid);
        
        String totalStat1 = readSystemStat();
        if (pidStat1 == null || totalStat1 == null) {
            return -1.f;
        }

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
            return -1.f;
        }

        String pidStat2 = readProcessStat(pid);
        String totalStat2 = readSystemStat();
        if (pidStat2 == null || totalStat2 == null) {
            return -1.f;
        }

        String[] toks = totalStat1.split(" ");
        long cpu1 = getSystemUptime(toks);
        
        toks = totalStat2.split(" ");
        long cpu2 = getSystemUptime(toks);
        
        return getProcessCpuUsage(pidStat1, pidStat2, cpu2 - cpu1);
    }
}