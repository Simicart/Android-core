package com.simicart.core.catalog.search.model;

public class ConstantsSearch {

	public static final String IMAGE = "image";
	public static String CHILD_CAT = "child_cat";

	public static String SPOT_IMAGE = "spot_image";
	public static String SPOT_ID = "spot_id";
	public static String SPOT_NAME = "spot_name";
	public static String SPOT_KEY = "spot_key";
	public static String TITLE = "title";
	public static String PRODUCT_ID_ARRAY = "product_id_array";
	// add param for search 
	public static String PARAM_QUERY = "key_word";
	public static String PARAM_KEY = "key";
	public static String PARAM_CATEGORY_ID = "category_id";
	public static String PARAM_CATEGORY_NAME = "category_name";
	public static String PARAM_OFFSET = "offset";
	public static String PARAM_LIMIT = "limit";
	public static String PARAM_SORT_OPTION = "sort_option";
	public static String PARAM_WIDTH = "width";
	public static String PARAM_HEIGHT = "height";
	public static String PARAM_URL = "URL";
	public static String PARAM_JSONFILTER = "filter";
	
	
	//url 
	public static String url_query = "connector/catalog/search_products";
	public static String url_category = "connector/catalog/get_category_products";
	public static String url_allcategory = "connector/catalog/get_all_products";
	public static String url_spot_matrixtheme = "themeone/api/get_spot_products";
	public static String url_spot_ztheme = "ztheme/api/get_spot_products";
}
