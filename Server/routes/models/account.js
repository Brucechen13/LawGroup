module.exports = init;

function init(mongoose){
	// default database config
	var host = 'mongodb://localhost/laws';

	// read database config form VCAP_SERVICES env
	if (process.env.VCAP_SERVICES) {
	  var mongodb_config = JSON.parse(process.env.VCAP_SERVICES).mongodb[0].credentials;
	  host = mongodb_config.host;
	  console.log('connect to coding net mongodb')
	}
	mongoose.connect(host, function(err){
		if(!err){
			console.log('connect to mongodb');
		}else{
			console.log('failed connect to mongodb');
			throw err;
		}
	});
	
	//积分规则
	var MsgScore = 20;//添加消息积分
	var CommentScore = 10;//添加评论积分
	var upMsgScore = 5;//点赞积分
	var costScore = 10;//操作消耗积分

	var Schema = mongoose.Schema, 
		ObjectId = Schema.ObjectId;
	var User = new Schema({
		_id:{type:String, unique:true,required:true,index: true},
		signature:{type:String},
		toupic:{type:String},
		name:{type:String},
		gender:{type:String},
		score:{type:Number,default: 0},
		msgs:[{type:ObjectId, ref:'Msg'}],
		samples:[{type:ObjectId, ref:'Sample'}],
		comments:[{type:ObjectId, ref:'comment'}],
		friends:[{type:String, ref:'User'}],
		tasks:[{type:ObjectId, ref:'Task'}]
	});

	var User = mongoose.model('User', User);

	var Msg = new Schema({
		msg:{
			title:{type:String},
			content:{type:String}
		},
		time:{type:Date, default: Date.now},
		user:{type:String, ref:'User'},
		upCount:{type:Number, default:0},
		ctCount:{type:Number, default:0},
		comments:[{type:ObjectId, ref:'comment'}]
	});
	var Msg = mongoose.model('Msg', Msg);

	var comment = new Schema({
		user:{type:String, ref:'User'},
		userName:{type:String},//populate无法多重查询，所以增加新的属性
		content:{type:String},
		time:{type:Date, default: Date.now}
	});
	var Comment = mongoose.model('comment', comment);
	
	var sample = new Schema({
		msg:{
			title:{type:String},
			content:{type:String}
		},
		time:{type:Date, default: Date.now},
		user:{type:String, ref:'User'},
		upCount:{type:Number, default:0},
		ctCount:{type:Number, default:0},
		comments:[{type:ObjectId, ref:'comment'}]
	});
	var Sample = mongoose.model('sample', sample);
	
	var Task = new Schema({
		user:{type:String, ref:'User'},
		content:{type:String},
		type:{type:String},
		time:{type:Date, default: Date.now}		
	});
	var Task = mongoose.model('Task', Task);

	var login = function(qq, callback){
		User.findById(qq, function(err, doc){
			if(err){
				console.log(err);
				return;
			}
			callback(doc);
		});
	};

	var newUser = function(qq, name, toupic, gender, callback){
		var user = new User({_id:qq, name:name, toupic:toupic, gender:gender});
		user.save(function(err){
			  if (err) {
				console.log('faile: '+err);  
				callback(err);
				return;
			  }
			  callback(null);
		 });
	};
	
	var userInfo = function(qq, callback){
		User.findById(qq,function(err,user){  
			   if(err){
				   console.log(err);
				}  
			   callback(user);
		  });
	};

	var updateUser = function(qq, name, value, callback){//返回是否错误码
		User.findById(qq,function(err,user){  
			   if(err){
					callback(err);
					return;
				}  
			   user[name] = value;
			   user.save(callback);
		  });
	};

	var addMsg = function(qq, title, content, callback){
		User.findById(qq,function(err,user){  
			   if(err){
					callback(err);
					return;
				}
			var msg = new Msg({user:user, 
				msg:{title:title,
						content:content}});
			msg.save(function(err){
				if(err){
					callback(err);
					return;
				}
				user.score += MsgScore;//增加积分
				user.msgs.push(msg);
				user.save();
				callback(null);
			});
		});
	};
	
	var addSample = function(qq, title, content, callback){
		User.findById(qq,function(err,user){  
			   if(err){
					callback(err);
					return;
				}
			var sample = new Sample({user:user, 
				msg:{title:title,
						content:content}});
			sample.save(function(err){
				if(err){
					callback(err);
					return;
				}
				user.score += MsgScore;//增加积分
				user.samples.push(sample);
				user.save();
				callback(null);
			});
		});
	};

	var addUpMsg = function(msgid, callback){
		Msg.findById(msgid, function (err, msg) {
				if(err){
					console.log(err);
					return callback(err);
				}
				msg.upCount += 1;
				msg.save(callback);
		});
	};

	var addMsgComment = function(qq, msgid, content, callback){
		User.findById(qq, function (err, user) {
			if(err){
				console.log(err);
				callback(err);
				return;
			}
			var comment = new Comment({content:content, user:qq, 
			userName:user.name});
			comment.save(function(err){
				if(err){
					console.log(err);
					callback(err);
					return;
				}
			});
			user.score += CommentScore;//增加积分
			user.comments.push(comment);
			user.save(function(err){
				if(err){
					console.log(err);
					callback(err);
					return;
				}
			});
			Msg.findById(msgid, function (err, msg) {
				if(err){
					console.log(err);
					callback(err);
					return;
				}
				msg.ctCount += 1;
				msg.comments.push(comment);
				msg.save(function(err){
					if(err){
						console.log(err);
						callback(err);
						return;
					}
				});
			});
			callback(null);
		});
	};
	
	var addSampleComment = function(qq, sampleid, content, callback){
		User.findById(qq, function (err, user) {
			if(err){
				console.log(err);
				callback(err);
				return;
			}
			var comment = new Comment({content:content, user:qq, 
			userName:user.name});
			comment.save(function(err){
				if(err){
					console.log(err);
					callback(err);
					return;
				}
			});
			user.score += CommentScore;//增加积分
			user.comments.push(comment);
			user.save(function(err){
				if(err){
					console.log(err);
					callback(err);
					return;
				}
			});
			Sample.findById(sampleid, function (err, sample) {
				if(err){
					console.log(err);
					callback(err);
					return;
				}
				sample.ctCount += 1;
				sample.comments.push(comment);
				sample.save(function(err){
					if(err){
						console.log(err);
						callback(err);
						return;
					}
				});
			});
			callback(null);
		});
	};

	var userFriends = function(qq, callback){
		User.findById(qq, "friends").populate("friends").exec(function(err, doc){
				if(err){
					console.log(err);
				}
				callback(doc);
			});
	};

	var removeMsg = function(msgid){
		Msg.findById(msgid, function (err, msg) {
			if(err){
				console.log(err);
				return;
			}
			User.findById(msg.user, function (err, user) {
				if(err){
					console.log(err);
					return;
				}
				user.msgs.remove(msg);
			});
			msg.comments.foreach(function(comment){
				comment.remove();
			});
			msg.remove();
		});
	};

	var removeComment = function(commentid){
		Comment.findById(commentid, function (err, comment) {
			User.findById(comment.user, function (err, user) {
				if(err){
					console.log(err);
					return;
				}
				user.comments.pull(comment);
			});
			Msg.findById(comment.msg, function (err, msg) {
				if(err){
					console.log(err);
					return;
				}
				msg.comments.pull(comment);
			});
			comment.remove();
		});
	};

	var removeFriend = function(qq, friendid, callback){
		User.findById(qq, function (err, user){
			if(err){
				console.log(err);
				callback(err);
				return;
			}
			user.friends.pull(friendid);
			user.save();
			console.log('the sub-doc was removed');
			callback(null);
		});
	};

	var userMsgs = function(qq, skip, callback){
		User.findById(qq, "msgs").populate({path: 'msgs',
		options: {limit: 10, skip: skip}}).exec(function(err, doc){
			if(err){
				console.log(err);
			}
			callback(doc);
		});
	};
	
	var allMsgs = function(skip, callback){
		Msg.find().skip(skip).limit(10).sort({'time':-1}).populate(
			{path: 'user', select: '_id name toupic'}).exec(function(err, doc){
			if(err){
				console.log(err);
			}
			callback(doc);
		});
	};

	var recentMsgs = function(date, callback){
		Msg.find().where('time').gt(date).sort({'time':-1})
			.populate({path: 'user', select: '_id name toupic'}).exec(function(err, doc){
			if(err){
				console.log(err);
			}
			callback(doc);
		});
	};
	
	var userSamples = function(qq, skip, callback){
		User.findById(qq, "samples").populate({path: 'samples',
		options: {limit: 10, skip: skip}}).exec(function(err, doc){
			if(err){
				console.log(err);
			}
			callback(doc);
		});
	};
	
	var allSamples = function(skip, callback){
		Sample.find().skip(skip).limit(10).sort({'time':-1}).populate(
			{path: 'user', select: '_id name toupic'}).exec(function(err, doc){
			if(err){
				console.log(err);
			}
			callback(doc);
		});
	};

	var recentSamples = function(date, callback){
		Sample.find().where('time').gt(date).sort({'time':-1})
			.populate({path: 'user', select: '_id name'}).exec(function(err, doc){
			if(err){
				console.log(err);
			}
			callback(doc);
		});
	};
	
	var MsgComments = function(msg, skip, callback){
		Msg.findById(msg, "comments").populate({path: 'comments ',
		options: {limit: 10, skip: skip,sort: {time: -1}}}).exec(function(err, doc){
			if(err){
				console.log(err);
			}
			callback(doc);
		});
	};
	
	var recentComments = function(date, callback){
		Msg.find().where('time').gt(date).sort({'time':-1})
			.populate({path: 'user', select: '_id name toupic'}).exec(function(err, doc){
			if(err){
				console.log(err);
			}
			callback(doc);
		});
	};

	var addTasks = function(sourceUser, type, content, destUser, callback){
		if(destUser!=null && sourceUser._id == destUser._id){
			return;
		}
		var task = new Task({user:sourceUser._id, content:content, type:type});
		task.save(function(err){
			if (err) {
				console.log(err);
				callback(err);				
				return;
			}
			if(destUser != null){
				destUser.tasks.push(task);
				destUser.save(callback);
			}else{
				User.update({}, {"$push": {"tasks":task}}, { multi: true },function (err, raw) {
					  if (err){
						  callback(err);
						  return;
					  }
					  callback(null);
				});
			}
		});
	};

	var addFriend = function(qq, callback){
		User.findById(qq, function(err, user){
			if(err || user.score < costScore){
				console.log(err);
				return callback(null);
			}
			User.findOne({city:user.city,
			_id:{$ne:user._id,$nin:user.friends}}, function(err, doc){
				if(err){
					console.log(err);
					return callback(null);
				}
				if(doc == null){//如果不存在符合条件的用户
					User.findOne({_id:{$ne:user._id,$nin:user.friends}}, 
					function(err, friend){
						if(err || friend == null){
							console.log(err);
							return callback(null);
						}
						user.score -= costScore;
						user.friends.push(friend);
						user.save();
						addTasks(user, '好友', '', friend, function(err){
							if(err){
								console.log(err);
								return callback(null);
							}
							return callback(friend);
						});
					});
				}else{
					user.score -= costScore;
					user.friends.push(doc);
					user.save();
					addTasks(user, '好友', '', doc, function(err){
						if(err){
							console.log(err);
							return callback(null);
						}
						return callback(doc);
					});
				}
			});
		});
	};
	
	var publishTask = function(qq, content, callback){
		User.findById(qq, function(err, user){
			if(err){
				callback(err);
				return;
			}
			addTasks(user, '任务', content, null, callback);
		});
	};
	
	var getTask = function(qq, callback){
		User.findById(qq, "tasks").populate("tasks").exec(function(err, doc){
			if(err || doc==null){
				console.log(err);
				return callback(null);
			}
			tasks = doc.tasks;
			console.log(tasks);
			if(tasks.length != 0){
				callback(tasks);
				for(var i = 0; i < tasks.length; i++){
					doc.tasks.remove(tasks[i]);
				}
				doc.save();
				return;
			}
			callback(null);
		});
	};
	
	return{
		login:login,//用户第三方登录
		newUser:newUser,//添加新用户
		updateUser:updateUser,//更新用户信息
		userInfo:userInfo,//查看用户信息
		addMsg:addMsg,//上传社区主题信息
		userMsgs:userMsgs,//查看用户上传的所有社区主题信息
		allMsgs:allMsgs,//查看社区主题信息
		recentMsgs:recentMsgs,//查看最新的社区主题信息
		addSample:addSample,//上传案例信息
		userSamples:userSamples,//查看用户上传的所有案例信息信息
		allSamples:allSamples,//查看案例信息信息
		recentSamples:recentSamples,//查看最新的案例信息信息
		addMsgComment:addMsgComment,//添加用户评论
		addSampleComment:addSampleComment,//添加案例评论
		addUpMsg:addUpMsg,//添加消息点赞
		MsgComments:MsgComments,//查看交通信息的评论
		addFriend:addFriend,//获取关联用户，添加好友
		removeFriend:removeFriend,//删除好友
		userFriends:userFriends,//查看用户好友
		publishTask:publishTask,//发布任务
		getTask:getTask//获取用户任务，使用长轮询查询
	};
};