var mongoose = require('mongoose');
var Schema = mongoose.Schema;

const postSchema = new Schema({
    content: {
        type: String,
    },
    like: {
        type: Number,
        default: 0,
    },
    image: {
        type: String,
        // type: Buffer,
        // contentsType: String,
    },
    walking: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Walking',
        required: true,    
    },
})

module.exports = mongoose.model('Post', postSchema);