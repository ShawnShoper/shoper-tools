package org.shoper.monitor;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.shoper.commons.OSUtil;
import org.shoper.commons.exception.ShoperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Get System informations.
 *
 * @author ShawnShoper
 */
public class SystemUtil {
    static Sigar sigar;
    static Logger logger = LoggerFactory.getLogger(SystemUtil.class);

    static {
        String libPath =  OSUtil.getJavaLibraryPath();
        String pathString = Thread.currentThread().getContextClassLoader()
                .getResource("sigar").toString().replace("file://", "").replace("file:/","/")
                .replace("jar:file:", "jar:");

        String osName = OSUtil.getOS();
        OSUtil.setProperties("java.library.path", libPath + (osName.contains("win")?";":":") + pathString);
        System.out.println(OSUtil.getJavaLibraryPath());
        if (logger.isDebugEnabled())
            logger.debug(String.valueOf(OSUtil.getJavaLibraryPath()));
        sigar = new Sigar();
    }

    /**
     * 获取内存信息
     *
     * @throws ShoperException
     */
    public static org.hyperic.sigar.Mem getMemInfo() throws ShoperException {
        try {
            return sigar.getMem();
        } catch (SigarException e) {
            throw new ShoperException(e);
        }
    }

    public static org.hyperic.sigar.CpuInfo[] getCpuInfoList()
            throws ShoperException {
        try {
            return sigar.getCpuInfoList();
        } catch (SigarException e) {
            throw new ShoperException(e);
        }
    }

    public static org.hyperic.sigar.Cpu[] getCpuList() throws ShoperException {
        try {
            return sigar.getCpuList();
        } catch (SigarException e) {
            throw new ShoperException(e);
        }
    }

    public static org.hyperic.sigar.CpuPerc getCpuPerc() throws ShoperException {
        try {
            return sigar.getCpuPerc();
        } catch (SigarException e) {
            throw new ShoperException(e);
        }
    }

    public static org.hyperic.sigar.CpuPerc[] getCpuPercList()
            throws ShoperException {
        try {
            return sigar.getCpuPercList();
        } catch (SigarException e) {
            throw new ShoperException(e);
        }
    }

    public static org.hyperic.sigar.FileSystem[] getFileSystems()
            throws ShoperException {
        try {
            return sigar.getFileSystemList();
        } catch (SigarException e) {
            throw new ShoperException(e);
        }
    }

    public static org.hyperic.sigar.NetInterfaceConfig getNetInterfaceConfig(
            String netName) throws ShoperException {
        try {
            return sigar.getNetInterfaceConfig(netName);
        } catch (SigarException e) {
            throw new ShoperException(e);
        }
    }

    public static org.hyperic.sigar.NetInterfaceStat getNetInterfaceStat(
            String netName) throws ShoperException {
        try {
            return sigar.getNetInterfaceStat(netName);
        } catch (SigarException e) {
            throw new ShoperException(e);
        }
    }

    public static String getNetAddress(String netName) throws ShoperException {
        return getNetInterfaceConfig(netName).getAddress();
    }

    public static org.hyperic.sigar.Cpu getCpuInfo() throws ShoperException {
        try {
            return sigar.getCpu();
        } catch (SigarException e) {
            throw new ShoperException(e);
        }
    }

    public static org.hyperic.sigar.CpuPerc getCpuPercInfo()
            throws ShoperException {
        try {
            return sigar.getCpuPerc();
        } catch (SigarException e) {
            throw new ShoperException(e);
        }
    }
}
