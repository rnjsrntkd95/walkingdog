const User = require("../models/user");
const Comment = require("../models/comment");
const Post = require("../models/post");

// 댓글 리스트 불러오기
exports.getCommentList = async (req, res, next) => {
  try {
    const comments = await Comment.find()
      .sort({ created: -1 })
      .populate("comment")
      .exec();
    res.json({
      comments,
    });
  } catch (err) {
    console.log(err);
  }
};

// 새로운 댓글 등록
exports.createComment = async (req, res, next) => {
  const { post_id, user_id, content } = req.body;
  console.log(req.body);

  // comment 저장
  try {
    const user = await User.findOne({ _id: user_id });
    const post = await Post.findOne({ _id: post_id });
    if (user) {
      const comment = await new Comment({
        user: user_id,
        content: content,
        post: post_id,
      });

      //populate 해서 _id참조하여 실제 객체 가져와야함
      console.log(comment);
    } else {
      res.json({
        error: "NO USER",
      });
    }
    res.json({
      resultComment,
    });
  } catch (err) {
    console.log(err);
    res.json({ error: 1 });
  }
};
