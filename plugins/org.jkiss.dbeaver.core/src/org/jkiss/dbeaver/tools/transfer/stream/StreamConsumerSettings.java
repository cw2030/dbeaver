/*
 * Copyright (C) 2010-2015 Serge Rieder
 * serge@jkiss.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.jkiss.dbeaver.tools.transfer.stream;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.operation.IRunnableContext;
import org.jkiss.dbeaver.model.data.DBDDataFormatterProfile;
import org.jkiss.dbeaver.registry.DataFormatterRegistry;
import org.jkiss.dbeaver.tools.transfer.IDataTransferSettings;
import org.jkiss.dbeaver.utils.ContentUtils;
import org.jkiss.utils.CommonUtils;

/**
 * Stream transfer settings
 */
public class StreamConsumerSettings implements IDataTransferSettings {

    enum LobExtractType {
        SKIP,
        FILES,
        INLINE
    }

    enum LobEncoding {
        BASE64,
        HEX,
        BINARY
    }

    private static final String PATTERN_TABLE = "{table}";
    private static final String PATTERN_TIMESTAMP = "{timestamp}";

    public static final String PROP_EXTRACT_IMAGES = "extractImages";
    public static final String PROP_FILE_EXTENSION = "extension";
    public static final String PROP_FORMAT = "format";

    private LobExtractType lobExtractType = LobExtractType.SKIP;
    private LobEncoding lobEncoding = LobEncoding.HEX;

    private String outputFolder = System.getProperty("user.home");
    private String outputFilePattern = PATTERN_TABLE + "_" + PATTERN_TIMESTAMP;
    private String outputEncoding = ContentUtils.getDefaultFileEncoding();
    private boolean outputEncodingBOM = true;

    private DBDDataFormatterProfile formatterProfile;

    private boolean outputClipboard = false;
    private boolean compressResults = false;
    private boolean openFolderOnFinish = true;

    public LobExtractType getLobExtractType() {
        return lobExtractType;
    }

    public void setLobExtractType(LobExtractType lobExtractType) {
        this.lobExtractType = lobExtractType;
    }

    public LobEncoding getLobEncoding() {
        return lobEncoding;
    }

    public void setLobEncoding(LobEncoding lobEncoding) {
        this.lobEncoding = lobEncoding;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    public String getOutputFilePattern() {
        return outputFilePattern;
    }

    public void setOutputFilePattern(String outputFilePattern) {
        this.outputFilePattern = outputFilePattern;
    }

    public String getOutputEncoding() {
        return outputEncoding;
    }

    public void setOutputEncoding(String outputEncoding) {
        this.outputEncoding = outputEncoding;
    }

    public boolean isOutputEncodingBOM() {
        return outputEncodingBOM;
    }

    public void setOutputEncodingBOM(boolean outputEncodingBOM) {
        this.outputEncodingBOM = outputEncodingBOM;
    }

    public boolean isOutputClipboard() {
        return outputClipboard;
    }

    public void setOutputClipboard(boolean outputClipboard) {
        this.outputClipboard = outputClipboard;
    }

    public boolean isCompressResults() {
        return compressResults;
    }

    public void setCompressResults(boolean compressResults) {
        this.compressResults = compressResults;
    }

    public boolean isOpenFolderOnFinish() {
        return openFolderOnFinish;
    }

    public void setOpenFolderOnFinish(boolean openFolderOnFinish) {
        this.openFolderOnFinish = openFolderOnFinish;
    }

    public DBDDataFormatterProfile getFormatterProfile() {
        return formatterProfile;
    }

    public void setFormatterProfile(DBDDataFormatterProfile formatterProfile) {
        this.formatterProfile = formatterProfile;
    }

    @Override
    public void loadSettings(IRunnableContext runnableContext, IDialogSettings dialogSettings) {
        if (!CommonUtils.isEmpty(dialogSettings.get("lobExtractType"))) {
            try {
                lobExtractType = LobExtractType.valueOf(dialogSettings.get("lobExtractType"));
            } catch (IllegalArgumentException e) {
                lobExtractType = LobExtractType.SKIP;
            }
        }
        if (!CommonUtils.isEmpty(dialogSettings.get("lobEncoding"))) {
            try {
                lobEncoding = LobEncoding.valueOf(dialogSettings.get("lobEncoding"));
            } catch (IllegalArgumentException e) {
                lobEncoding = LobEncoding.HEX;
            }
        }

        if (!CommonUtils.isEmpty(dialogSettings.get("outputFolder"))) {
            outputFolder = dialogSettings.get("outputFolder");
        }
        if (!CommonUtils.isEmpty(dialogSettings.get("outputFilePattern"))) {
            outputFilePattern = dialogSettings.get("outputFilePattern");
        }
        if (!CommonUtils.isEmpty(dialogSettings.get("outputEncoding"))) {
            outputEncoding = dialogSettings.get("outputEncoding");
        }
        if (!CommonUtils.isEmpty(dialogSettings.get("outputEncodingBOM"))) {
            outputEncodingBOM = dialogSettings.getBoolean("outputEncodingBOM");
        }
        if (!CommonUtils.isEmpty(dialogSettings.get("outputClipboard"))) {
            outputClipboard = dialogSettings.getBoolean("outputClipboard");
        }

        if (!CommonUtils.isEmpty(dialogSettings.get("compressResults"))) {
            compressResults = dialogSettings.getBoolean("compressResults");
        }
        if (dialogSettings.get("openFolderOnFinish") != null) {
            openFolderOnFinish = dialogSettings.getBoolean("openFolderOnFinish");
        }

        if (!CommonUtils.isEmpty(dialogSettings.get("formatterProfile"))) {
            formatterProfile = DataFormatterRegistry.getInstance().getCustomProfile(dialogSettings.get("formatterProfile"));
        }
/*
        IDialogSettings[] expSections = dialogSettings.getSections();
        if (expSections != null && expSections.length > 0) {
            for (IDialogSettings expSection : expSections) {
                expId = expSection.getName();
                IStreamDataExporterDescriptor exporter = DBeaverCore.getInstance().getDataTransferRegistry().getDataExporter(expId);
                if (exporter != null) {
                    Map<Object, Object> expProps = new HashMap<Object, Object>();
                    exporterPropsHistory.put(exporter, expProps);
                    for (IPropertyDescriptor prop : exporter.getProperties()) {
                        Object value = expSection.get(prop.getId().toString());
                        if (value != null) {
                            if ("extractImages".equals(prop.getId())) {
                                value = Boolean.parseBoolean((String) value);
                            }
                            expProps.put(prop.getId(), value);
                        }
                    }
                }
            }
        }
        setExporterDescriptor(dataExporter);
*/
    }

    @Override
    public void saveSettings(IDialogSettings dialogSettings) {
        dialogSettings.put("lobExtractType", lobExtractType.name());
        dialogSettings.put("lobEncoding", lobEncoding.name());

        dialogSettings.put("outputFolder", outputFolder);
        dialogSettings.put("outputFilePattern", outputFilePattern);
        dialogSettings.put("outputEncoding", outputEncoding);
        dialogSettings.put("outputEncodingBOM", outputEncodingBOM);
        dialogSettings.put("outputClipboard", outputClipboard);

        dialogSettings.put("compressResults", compressResults);
        dialogSettings.put("openFolderOnFinish", openFolderOnFinish);

        if (formatterProfile != null) {
            dialogSettings.put("formatterProfile", formatterProfile.getProfileName());
        } else {
            dialogSettings.put("formatterProfile", "");
        }
    }

}
