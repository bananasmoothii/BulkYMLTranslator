package fr.bananasmoothii.bulkymltranslator;

import fr.bananasmoothii.bulkymltranslator.TranslationNode.Language;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Project extends MainReference {

    public final Config config;
    public final File directory;

    public Project(@NotNull File directory, Main mainReference) {
        this.main = mainReference;

        if (! directory.isDirectory()) throw new IllegalArgumentException("Not a directory");
        this.directory = directory;

        try {
            Yaml yaml = new Yaml();

            File configFile = new File(directory.getPath() + "/BulkYMLTranslator.yml");
            if (! configFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                configFile.createNewFile();
                config = new Config(Language.ENGLISH, null, main.preferredFileEncoding);
                rescanDirectories();
                yaml.dump(config, new FileWriter(configFile));
            }
            else {
                config = yaml.load(new FileReader(configFile));
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static class Config {
        public List<TranslationNode> translationNodes;
        public Language sourceLanguage;
        public Language destinationLanguage;
        public List<Keyword> keywords;
        public String fileEncoding;
        public List<File> fileList;
        public boolean useFileListAsWhitelist;
        public List<String> yamlPaths;
        public boolean useYamlPathsAsWhitelist;

        public Config() {
            this(null);
        }

        public Config(Language destinationLanguage) {
            this(Language.ENGLISH, destinationLanguage, "UTF-8");
        }

        public Config(Language sourceLanguage, Language destinationLanguage, String fileEncoding) {
            this.fileEncoding = fileEncoding;
            this.sourceLanguage = sourceLanguage;
            this.destinationLanguage = destinationLanguage;
            keywords = new ArrayList<>();
            fileList = new ArrayList<>();
            yamlPaths = new ArrayList<>();
            translationNodes = new ArrayList<>();
        }
    }

    protected void rescanDirectories() {
        try {
            scanFileOrDirectory(directory);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void scanFileOrDirectory(File file) throws FileNotFoundException {
        if (file.isFile()) {
            if (file.getName().endsWith(".yml") && ! file.getName().equals("BulkYMLTranslator.yml")) {
                Yaml yaml = new Yaml();
                Object loaded = yaml.load(new FileReader(file));
                scanYamlObject(file, null, loaded, new ArrayList<>());
            }
        } else if (file.isDirectory()) {
            for (File listedFile : file.listFiles()) {
                scanFileOrDirectory(listedFile);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void scanYamlObject(File file, Object parent, Object object, List<Object> indexPaths) {
        if (object instanceof Map map) {
            for (Map.Entry<String, Object> entry : (Set<Map.Entry<String, Object>>) map.entrySet()) {
                indexPaths.add(entry.getKey());
                scanYamlObject(file, map, entry.getValue(), indexPaths);
                indexPaths.remove(indexPaths.size() - 1);
            }
        } else if (object instanceof List list) {
            for (int i = 0; i < list.size(); i++) {
                indexPaths.add(i);
                scanYamlObject(file, list, list.get(i), indexPaths);
                indexPaths.remove(indexPaths.size() - 1);
            }
        } else if (object instanceof String string) {
            config.translationNodes.add(new TranslationNode(file, List.copyOf(indexPaths), string));
        }
    }
}
