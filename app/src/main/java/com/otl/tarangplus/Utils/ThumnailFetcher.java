package com.otl.tarangplus.Utils;

import android.text.TextUtils;

import com.otl.tarangplus.model.Thumbnails;

public class ThumnailFetcher {

    public static String fetchAppropriateThumbnail(Thumbnails thumnail, String layoutType) {
        String thumnailUrl = null;
        if (thumnail != null) {



            if (Constants.T_CONTINUE_WATCHING.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage169 lXLarge = thumnail.getXlImage169();
                    return lXLarge.getUrl();
                }
                Thumbnails.Medium169 lMedium = thumnail.getMedium169();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Large169 lLarge = thumnail.getLarge169();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_2_3_MOVIE.equalsIgnoreCase(layoutType)
            || Constants.T_2_3_BIG_META.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage23 lXLarge = thumnail.getXlImage23();
                    return lXLarge.getUrl();
                }
                Thumbnails.Medium23 lMedium = thumnail.getMedium23();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Large23 lLarge = thumnail.getLarge23();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_2_3_MOVIE_STATIC.equalsIgnoreCase(layoutType)) {

                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage23 lXLarge = thumnail.getXlImage23();
                    return lXLarge.getUrl();
                }

                Thumbnails.Medium23 lMedium = thumnail.getMedium23();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Large23 lLarge = thumnail.getLarge23();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_16_9_BIG.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage169 lXLarge = thumnail.getXlImage169();
                    return lXLarge.getUrl();
                }

                Thumbnails.Large169 lLarge = thumnail.getLarge169();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_16_9_BIG_RIGHT_META.equalsIgnoreCase(layoutType) ||
                    Constants.T_16_9_BIG_WITHOUGHT_META.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage169 lXLarge = thumnail.getXlImage169();
                    return lXLarge.getUrl();
                }

                Thumbnails.Large169 lLarge = thumnail.getLarge169();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_16_9_BIG_META.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage169 lXLarge = thumnail.getXlImage169();
                    return lXLarge.getUrl();
                }

                Thumbnails.Large169 lLarge = thumnail.getLarge169();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_16_9_SMALL.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage169 lXLarge = thumnail.getXlImage169();
                    return lXLarge.getUrl();
                }
                Thumbnails.Medium169 lMedium = thumnail.getMedium169();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Large169 lLarge = thumnail.getLarge169();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_16_9_LIVEBANNER.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage169 lXLarge = thumnail.getXlImage169();
                    return lXLarge.getUrl();
                }
                Thumbnails.Medium169 lMedium = thumnail.getMedium169();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Large169 lLarge = thumnail.getLarge169();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_16_9_SMALL_META_LAYOUT.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage169 lXLarge = thumnail.getXlImage169();
                    return lXLarge.getUrl();
                }
                Thumbnails.Medium169 lMedium = thumnail.getMedium169();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Large169 lLarge = thumnail.getLarge169();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_1_1_PLAIN.equalsIgnoreCase(layoutType) ||
                    Constants.T_1_1_ALBUM_META.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage11 lXLarge = thumnail.getXlImage11();
                    return lXLarge.getUrl();
                }
                Thumbnails.Medium11 lMedium = thumnail.getMedium11();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Large11 lLarge = thumnail.getLarge11();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_1_1_ALBUM.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage11 lXLarge = thumnail.getXlImage11();
                    return lXLarge.getUrl();
                }
                Thumbnails.Medium11 lMedium = thumnail.getMedium11();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Large11 lLarge = thumnail.getLarge11();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_16_9_BANNER.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isTablet();
                if (isHighResolution) {
                    Thumbnails.XLImage169 lXLarge = thumnail.getXlImage169();
                    return lXLarge.getUrl();
                }

                Thumbnails.Large169 lLarge = thumnail.getLarge169();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }

            if (Constants.T_16_9_BANNER.equalsIgnoreCase(layoutType) || Constants.T_SUBSCRIPTION.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isTablet();
                if (isHighResolution) {
                    Thumbnails.XLImage169 lXLarge = thumnail.getXlImage169();
                    return lXLarge.getUrl();
                }

                Thumbnails.Large169 lLarge = thumnail.getLarge169();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }

                Thumbnails.Small169 small169 = thumnail.getSmall169();
                if (small169 != null) {
                    String url = small169.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Medium169 lMedium = thumnail.getMedium169();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }

            } else if ("movies".equalsIgnoreCase(layoutType)) {
                Thumbnails.Small23 small23 = thumnail.getSmall23();
                if (small23 != null) {
                    String url = small23.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Medium23 lMedium = thumnail.getMedium23();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Large23 lLarge = thumnail.getLarge23();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            } else if ("shows".equalsIgnoreCase(layoutType)) {
                Thumbnails.Small169 small169 = thumnail.getSmall169();
                if (small169 != null) {
                    String url = small169.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Medium169 lMedium = thumnail.getMedium169();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Large169 lLarge = thumnail.getLarge169();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            } else {
                Thumbnails.PSmall pSmall = thumnail.getPSmall();
                if (pSmall != null) {
                    String url = pSmall.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.LMedium lMedium = thumnail.getLMedium();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.LLarge lLarge = thumnail.getLLarge();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
        }
        return thumnailUrl;
    }


    public static String fetchAppropriateThumbnailForCurosal(Thumbnails thumnail, String layoutType) {
        String thumnailUrl = null;
        if (thumnail != null) {

            try{
                if (thumnail.getListItemBanner() != null && !TextUtils.isEmpty(thumnail.getListItemBanner().getUrl())) {
                    Thumbnails.ListItemBanner itemBanner = thumnail.getListItemBanner();
                    return itemBanner.getUrl();
                }
            }catch (Exception e){
            }

            if (Constants.T_CONTINUE_WATCHING.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage169 lXLarge = thumnail.getXlImage169();
                    return lXLarge.getUrl();
                }
                Thumbnails.Medium169 lMedium = thumnail.getMedium169();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Large169 lLarge = thumnail.getLarge169();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_2_3_MOVIE.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage23 lXLarge = thumnail.getXlImage23();
                    return lXLarge.getUrl();
                }
                Thumbnails.Medium23 lMedium = thumnail.getMedium23();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Large23 lLarge = thumnail.getLarge23();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_2_3_MOVIE_STATIC.equalsIgnoreCase(layoutType)) {

                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage23 lXLarge = thumnail.getXlImage23();
                    return lXLarge.getUrl();
                }

                Thumbnails.Medium23 lMedium = thumnail.getMedium23();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Large23 lLarge = thumnail.getLarge23();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_16_9_BIG.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage169 lXLarge = thumnail.getXlImage169();
                    return lXLarge.getUrl();
                }

                Thumbnails.Large169 lLarge = thumnail.getLarge169();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_16_9_BIG_META.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage169 lXLarge = thumnail.getXlImage169();
                    return lXLarge.getUrl();
                }

                Thumbnails.Large169 lLarge = thumnail.getLarge169();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_16_9_SMALL.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage169 lXLarge = thumnail.getXlImage169();
                    return lXLarge.getUrl();
                }
                Thumbnails.Medium169 lMedium = thumnail.getMedium169();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Large169 lLarge = thumnail.getLarge169();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_16_9_SMALL_META_LAYOUT.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage169 lXLarge = thumnail.getXlImage169();
                    return lXLarge.getUrl();
                }
                Thumbnails.Medium169 lMedium = thumnail.getMedium169();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Large169 lLarge = thumnail.getLarge169();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_1_1_PLAIN.equalsIgnoreCase(layoutType) ||
                    Constants.T_1_1_ALBUM_META.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage11 lXLarge = thumnail.getXlImage11();
                    return lXLarge.getUrl();
                }
                Thumbnails.Medium11 lMedium = thumnail.getMedium11();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Large11 lLarge = thumnail.getLarge11();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_1_1_ALBUM.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isHighResulutionDevice();
                if (isHighResolution) {
                    Thumbnails.XLImage11 lXLarge = thumnail.getXlImage11();
                    return lXLarge.getUrl();
                }
                Thumbnails.Medium11 lMedium = thumnail.getMedium11();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Large11 lLarge = thumnail.getLarge11();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
            if (Constants.T_16_9_BANNER.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isTablet();
                if (isHighResolution) {
                    Thumbnails.XLImage169 lXLarge = thumnail.getXlImage169();
                    return lXLarge.getUrl();
                }

                Thumbnails.Large169 lLarge = thumnail.getLarge169();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }

            if (Constants.T_16_9_BANNER.equalsIgnoreCase(layoutType) || Constants.T_SUBSCRIPTION.equalsIgnoreCase(layoutType)) {
                boolean isHighResolution = Helper.isTablet();
                if (isHighResolution) {
                    Thumbnails.XLImage169 lXLarge = thumnail.getXlImage169();
                    return lXLarge.getUrl();
                }

                Thumbnails.Large169 lLarge = thumnail.getLarge169();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }

                Thumbnails.Small169 small169 = thumnail.getSmall169();
                if (small169 != null) {
                    String url = small169.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Medium169 lMedium = thumnail.getMedium169();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }

            } else if ("movies".equalsIgnoreCase(layoutType)) {
                Thumbnails.Small23 small23 = thumnail.getSmall23();
                if (small23 != null) {
                    String url = small23.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Medium23 lMedium = thumnail.getMedium23();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Large23 lLarge = thumnail.getLarge23();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            } else if ("shows".equalsIgnoreCase(layoutType)) {
                Thumbnails.Small169 small169 = thumnail.getSmall169();
                if (small169 != null) {
                    String url = small169.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Medium169 lMedium = thumnail.getMedium169();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.Large169 lLarge = thumnail.getLarge169();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            } else {
                Thumbnails.PSmall pSmall = thumnail.getPSmall();
                if (pSmall != null) {
                    String url = pSmall.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.LMedium lMedium = thumnail.getLMedium();
                if (lMedium != null) {
                    String url = lMedium.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
                Thumbnails.LLarge lLarge = thumnail.getLLarge();
                if (lLarge != null) {
                    String url = lLarge.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        return url;
                    }
                }
            }
        }
        return thumnailUrl;
    }


}
