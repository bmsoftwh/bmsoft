package com.bmsoft.common.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * swagger2 属性配置
 *
 */
//@RefreshScope
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {
    /**
     * 是否开启swagger
     **/
    private Boolean enabled = true;

    /**
     * 是否生产环境
     */
    private Boolean production = false;
    /**
     * 离线文档路径
     */
    private Markdown markdown = new Markdown();


    /**
     * 访问账号密码
     */
    private Basic basic = new Basic();

    /**
     * 标题
     **/
    private String title = "在线文档";
    private String group = "";
    /**
     * 描述
     **/
    private String description = "bmsoft 在线文档";
    /**
     * 版本
     **/
    private String version = "1.0";
    /**
     * 许可证
     **/
    private String license = "";
    /**
     * 许可证URL
     **/
    private String licenseUrl = "";
    /**
     * 服务条款URL
     **/
    private String termsOfServiceUrl = "";

    private Contact contact = new Contact();

    /**
     * swagger会解析的包路径
     **/
    private String basePackage = "com.bmsoft";

    /**
     * swagger会解析的url规则
     **/
    private List<String> basePath = new ArrayList<>();
    /**
     * 在basePath基础上需要排除的url规则
     **/
    private List<String> excludePath = new ArrayList<>();

    /**
     * 分组文档
     **/
    private Map<String, DocketInfo> docket = new LinkedHashMap<>();

    /**
     * host信息
     **/
    private String host = "";

    /**
     * 排序
     */
    private Integer order = 1;

    /**
     * 全局参数配置
     **/
    private List<GlobalOperationParameter> globalOperationParameters;

    public String getGroup() {
        if (group == null || "".equals(group)) {
            return title;
        }
        return group;
    }
    
    

    public Boolean getEnabled() {
		return enabled;
	}


	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}


	public Boolean getProduction() {
		return production;
	}


	public void setProduction(Boolean production) {
		this.production = production;
	}


	public Markdown getMarkdown() {
		return markdown;
	}


	public void setMarkdown(Markdown markdown) {
		this.markdown = markdown;
	}


	public Basic getBasic() {
		return basic;
	}


	public void setBasic(Basic basic) {
		this.basic = basic;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public String getLicense() {
		return license;
	}


	public void setLicense(String license) {
		this.license = license;
	}


	public String getLicenseUrl() {
		return licenseUrl;
	}


	public void setLicenseUrl(String licenseUrl) {
		this.licenseUrl = licenseUrl;
	}


	public String getTermsOfServiceUrl() {
		return termsOfServiceUrl;
	}


	public void setTermsOfServiceUrl(String termsOfServiceUrl) {
		this.termsOfServiceUrl = termsOfServiceUrl;
	}


	public Contact getContact() {
		return contact;
	}


	public void setContact(Contact contact) {
		this.contact = contact;
	}


	public String getBasePackage() {
		return basePackage;
	}


	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}


	public List<String> getBasePath() {
		return basePath;
	}


	public void setBasePath(List<String> basePath) {
		this.basePath = basePath;
	}


	public List<String> getExcludePath() {
		return excludePath;
	}


	public void setExcludePath(List<String> excludePath) {
		this.excludePath = excludePath;
	}


	public Map<String, DocketInfo> getDocket() {
		return docket;
	}


	public void setDocket(Map<String, DocketInfo> docket) {
		this.docket = docket;
	}


	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public Integer getOrder() {
		return order;
	}


	public void setOrder(Integer order) {
		this.order = order;
	}


	public List<GlobalOperationParameter> getGlobalOperationParameters() {
		return globalOperationParameters;
	}


	public void setGlobalOperationParameters(List<GlobalOperationParameter> globalOperationParameters) {
		this.globalOperationParameters = globalOperationParameters;
	}


	public void setGroup(String group) {
		this.group = group;
	}



	public static class GlobalOperationParameter {
        /**
         * 参数名
         **/
        private String name;

        /**
         * 描述信息
         **/
        private String description = "全局参数";

        /**
         * 指定参数类型
         **/
        private String modelRef = "String";

        /**
         * 参数放在哪个地方:header,query,path,body.form
         **/
        private String parameterType = "header";

        /**
         * 参数是否必须传
         **/
        private Boolean required = false;
        /**
         * 默认值
         */
        private String defaultValue = "";
        /**
         * 允许为空
         */
        private Boolean allowEmptyValue = true;
        /**
         * 排序
         */
        private int order = 1;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getModelRef() {
			return modelRef;
		}
		public void setModelRef(String modelRef) {
			this.modelRef = modelRef;
		}
		public String getParameterType() {
			return parameterType;
		}
		public void setParameterType(String parameterType) {
			this.parameterType = parameterType;
		}
		public Boolean getRequired() {
			return required;
		}
		public void setRequired(Boolean required) {
			this.required = required;
		}
		public String getDefaultValue() {
			return defaultValue;
		}
		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}
		public Boolean getAllowEmptyValue() {
			return allowEmptyValue;
		}
		public void setAllowEmptyValue(Boolean allowEmptyValue) {
			this.allowEmptyValue = allowEmptyValue;
		}
		public int getOrder() {
			return order;
		}
		public void setOrder(int order) {
			this.order = order;
		}
        
    }

    public static class DocketInfo {
        /**
         * 标题
         **/
        private String title = "在线文档";
        /**
         * 自定义组名
         */
        private String group = "";
        /**
         * 描述
         **/
        private String description = "bmsoft 在线文档";
        /**
         * 版本
         **/
        private String version = "1.0";
        /**
         * 许可证
         **/
        private String license = "";
        /**
         * 许可证URL
         **/
        private String licenseUrl = "";
        /**
         * 服务条款URL
         **/
        private String termsOfServiceUrl = "";

        private Contact contact = new Contact();

        /**
         * swagger会解析的包路径
         **/
        private String basePackage = "";

        /**
         * swagger会解析的url规则
         **/
        private List<String> basePath = new ArrayList<>();
        /**
         * 在basePath基础上需要排除的url规则
         **/
        private List<String> excludePath = new ArrayList<>();

        private List<GlobalOperationParameter> globalOperationParameters;

        /**
         * 排序
         */
        private Integer order = 1;

        public String getGroup() {
            if (group == null || "".equals(group)) {
                return title;
            }
            return group;
        }

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getLicense() {
			return license;
		}

		public void setLicense(String license) {
			this.license = license;
		}

		public String getLicenseUrl() {
			return licenseUrl;
		}

		public void setLicenseUrl(String licenseUrl) {
			this.licenseUrl = licenseUrl;
		}

		public String getTermsOfServiceUrl() {
			return termsOfServiceUrl;
		}

		public void setTermsOfServiceUrl(String termsOfServiceUrl) {
			this.termsOfServiceUrl = termsOfServiceUrl;
		}

		public Contact getContact() {
			return contact;
		}

		public void setContact(Contact contact) {
			this.contact = contact;
		}

		public String getBasePackage() {
			return basePackage;
		}

		public void setBasePackage(String basePackage) {
			this.basePackage = basePackage;
		}

		public List<String> getBasePath() {
			return basePath;
		}

		public void setBasePath(List<String> basePath) {
			this.basePath = basePath;
		}

		public List<String> getExcludePath() {
			return excludePath;
		}

		public void setExcludePath(List<String> excludePath) {
			this.excludePath = excludePath;
		}

		public List<GlobalOperationParameter> getGlobalOperationParameters() {
			return globalOperationParameters;
		}

		public void setGlobalOperationParameters(List<GlobalOperationParameter> globalOperationParameters) {
			this.globalOperationParameters = globalOperationParameters;
		}

		public Integer getOrder() {
			return order;
		}

		public void setOrder(Integer order) {
			this.order = order;
		}

		public void setGroup(String group) {
			this.group = group;
		}
        
        
    }


    public static class Contact {
        /**
         * 联系人
         **/
        private String name = "bmsoft";
        /**
         * 联系人url
         **/
        private String url = "https://www.bmsoft.com.cn";
        /**
         * 联系人email
         **/
        private String email = "wangzihong@bmsoft.com.cn";
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
        
        
    }

    public static class Basic {
        private Boolean enable = false;
        private String username = "bmsoft";
        private String password = "bmsoft";
		public Boolean getEnable() {
			return enable;
		}
		public void setEnable(Boolean enable) {
			this.enable = enable;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
        
    }


    public static class Markdown {
        private Boolean enable = false;
        private String basePath = "classpath:markdown/*";
		public Boolean getEnable() {
			return enable;
		}
		public void setEnable(Boolean enable) {
			this.enable = enable;
		}
		public String getBasePath() {
			return basePath;
		}
		public void setBasePath(String basePath) {
			this.basePath = basePath;
		}
        
    }
}
