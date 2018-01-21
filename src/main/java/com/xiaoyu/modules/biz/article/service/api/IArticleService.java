/**
 * 不要因为走了很远就忘记当初出发的目的:whatever happened,be yourself
 */
package com.xiaoyu.modules.biz.article.service.api;

import javax.servlet.http.HttpServletRequest;

/**
 * 2017年5月22日下午5:20:01
 * 
 * @author xiaoyu
 * @description
 */
public interface IArticleService {

    /**
     * 文章详情
     * 
     * @param articleId
     * @return
     */
    public String detail(String articleId);

    /**
     * 热门列表
     * 
     * @param request
     * @return
     */
    public String hotList(HttpServletRequest request);

    /**
     * 查询列表
     * 
     * @param request
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public String list(HttpServletRequest request, String userId, Integer pageNum, Integer pageSize);

    /**
     * 收藏列表
     * 
     * @param request
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public String collectList(HttpServletRequest request, String userId, Integer pageNum, Integer pageSize);

    /**
     * 增加文章
     * 
     * @param request
     * @param title
     * @param content
     * @param userId
     * @param token
     * @return
     */
    public String addArticle(HttpServletRequest request, String title, String content, String userId, String token);

    /**编辑文章
     * @param request
     * @param title
     * @param content
     * @param userId
     * @param token
     * @param token 
     * @return
     */
    public String editArticle(HttpServletRequest request, String title, String content, String userId, String articleId, String token);
    /**
     * 增加阅读数
     * 
     * @param request
     * @param articleId
     * @return
     */
    public String addReadNum(HttpServletRequest request, String articleId);

    /**
     * 点赞
     * 
     * @param request
     * @param articleId
     * @param isLike
     * @return
     */
    public String addLike(HttpServletRequest request, String articleId, Integer isLike);

    /**
     * 评论点赞
     * 
     * @param request
     * @param commentId
     * @param isLike
     * @return
     */
    public String addCommentLike(HttpServletRequest request, String commentId, Integer isLike);

    /**
     * 收藏
     * 
     * @param request
     * @param articleId
     * @param isCollect
     * @return
     */
    public String addCollect(HttpServletRequest request, String articleId, Integer isCollect);

    /**
     * 评论
     * 
     * @param request
     * @param articleId
     * @param content
     * @return
     */
    public String comment(HttpServletRequest request, String articleId, String content);
    
    /**
     * 回复
     * 
     * @param request
     * @param commentId
     * @param replyContent
     * @return
     */
    public String reply(HttpServletRequest request, String commentId, String replyContent);

    /**
     * 评论列表
     * 
     * @param request
     * @param articleId
     * @param pageNum
     * @return
     */
    public String comments(HttpServletRequest request, String articleId, Integer pageNum);

    /**
     * 最新评论
     * 
     * @param request
     * @param articleId
     * @return
     */
    public String newComments(HttpServletRequest request, String articleId);

    /**
     * 最新文章
     * 
     * @param request
     * @param userIds
     * @return
     */
    public String latestOfUsers(HttpServletRequest request, String[] userIds);

    /**
     * 搜索
     * 
     * @param request
     * @param keyword
     * @return
     */
    public String search(HttpServletRequest request, String keyword);

    /**
     * 同步到es
     * 
     * @return
     */
    public void synElastic();

}
