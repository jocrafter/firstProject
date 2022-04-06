import "./style.css";
import {getApi, getImg, postApi, postImg, postLogin} from "./API";


/**
 * check if localeStorage is not null
 * than start viewuserLogin with name
 * and start renderCommentsTable
 */
window.onload = function () {

    if (localStorage.getItem("name") != null) {
        viewUserLogin(localStorage.getItem("name"))
        viewTopic(true);
        console.log(localStorage.getItem("topicTopic"))

    } else {
        viewUserLogin(null)
    }
    document.getElementById("name").value = ""
    document.getElementById("search-input").value = ""
    document.getElementById("registerName").value = ""
    document.getElementById("comment").value = ""
    document.getElementById("birthday").value = ""
    document.getElementById("password").value = ""
    localStorage.setItem("topicTopic", null)
    localStorage.setItem("topicId", null)
}


/**
 * read the Name and Password
 * start handleResponse , viewUserLogin and renderCommentsTable
 * @returns
 */
window.login = async function () {
    let user = {
        name: document.getElementById("name").value,
        password: document.getElementById("password").value
    }
    let path = "user/login"
    await sendLogin(path, user)

}

async function sendLogin(path, user) {
    try {
        let response = await postLogin(path, user)

        console.log(response);
        localStorage.clear();
        localStorage.setItem("name", response.name)
        localStorage.setItem("jwt", response.jwt)
        console.log("name set " + response.name)
        viewUserLogin(localStorage.getItem("name"))

    } catch (error) {
        error.json.then(errorBody => {
            alert(errorBody.message)
        })
    }
}

/**
 * read Name, Password and Birthday
 * start handleResponse and switch window to Home
 * start viewUserLogin
 */
window.registerUser = async function () {
    let user = {
        name: document.getElementById("registerName").value,
        password: document.getElementById("registerPassword").value,
        birthday: document.getElementById("birthday").value,
    }
    let path = "user/register"
    await sendLogin(path, user)
}

/**
 * Post a Comment on ./comment
 * and start renderCommentsTable
 */
window.postComment=function() {
    let comment = {
        comment: document.getElementById("comment").value
    }
    let path = "comments/" + localStorage.getItem("topicTopic");
    postApi(path, comment)
        .then(() => renderCommentsTable())
        .then(() => document.getElementById("comment").value = "")
}

/**
 * fetchComments from ./comments
 * @returns Comments
 */
window.fetchComments = async () => {
    let path = 'comments/' + localStorage.getItem("topicTopic") + "/?search=" + document.getElementById("searchName").value;
    let response = await getApi(path)
    return response;
}


/**
 * fetchcomments create an clear table
 * and add the Comments to the table
 * @returns {Promise<void>}
 */

window.renderCommentsTable = async () => {
    const comments = await fetchComments()
    console.log(comments + "comments")
    const table = document.getElementById('table');
    table.innerHTML = ""
    for (let i = 0; i < comments.length; i++) {
        let contentUser = comments[i].name,
            contentComment = comments[i].comment,
            contentDate = comments[i].date,
            contentid = comments[i].id,
            contentlike = comments[i].likes
        let row = table.insertRow();
        let userCell = row.insertCell();
        let commentCell = row.insertCell();
        let dateCell = row.insertCell();
        let likeCell = row.insertCell();
        userCell.appendChild(document.createTextNode(contentUser))
        commentCell.appendChild(document.createTextNode(contentComment))
        dateCell.appendChild(document.createTextNode(contentDate))
        var b = document.createElement("INPUT")
        b.setAttribute("type", "button")
        b.setAttribute("class", "like")
        b.id = contentid;
        b.setAttribute("value", contentlike + "")
        b.setAttribute("onclick", "likeComment(this.id)")
        likeCell.append(b)
    }
}

/**
 * Switch between user login view with Username and
 * no user Login with Login and Register.
 * @param userName
 */
window.viewUserLogin = function (userName) {
    if (localStorage.getItem("name") != null) {
        document.getElementById("noLogin").hidden = true
        document.getElementById("userLogin").hidden = false
        document.getElementById("loggedInUsername").innerText = "User : " + userName
        document.getElementById("ifLogin").hidden = false
        document.getElementById("search-input").value = ""
        document.getElementById("showTopic").hidden = true
        document.getElementById("showCreateTopic").hidden = true
        document.getElementById("table").hidden = true
    } else {
        document.getElementById("noLogin").hidden = false
        document.getElementById("userLogin").hidden = true
        document.getElementById("ifLogin").hidden = true
        document.getElementById("table").hidden = true

    }

}

window.viewTopic = async function (tmp) {
    if (tmp != null) {
        let topic = localStorage.getItem("topicTopic")
        console.log("topic")
        topic = await getTopic(topic);
        renderCommentsTable(topic.topic);

        if (localStorage.getItem("imgName") != undefined) {
            console.log(localStorage.getItem("imgName"))
            try {
                await loadPicture()
                document.getElementById("picture").hidden = false
                document.getElementById("picture").src = localStorage.getItem("imgUrl")
            } catch {
                document.getElementById("picture").hidden = true
            }
        } else {
            document.getElementById("picture").hidden = true
        }
        document.getElementById("showTopic").hidden = false;
        document.getElementById("table").hidden = false
        document.getElementById("showCreateTopic").hidden = true;


    }
    if (tmp) {

    } else {
        document.getElementById("picture").src = null
        document.getElementById("showTopic").hidden = true;
        document.getElementById("showCreateTopic").hidden = false;


        renderCommentsTable();
    }
}

/**
 * clear localSorege and the table
 * start viewuserLogin
 */
window.userLogout = function () {
    localStorage.clear()
    viewUserLogin()
    const table = document.getElementById('table');
    table.innerHTML = ""
}

/**
 * Check to runtime the length of Username and Password
 * and check is Birthday entered
 * than set the Register Button on able
 */
window.allEntered = function() {
    if (document.getElementById("registerName").value && document.getElementById("registerName").value.length >= 4) {
        if (document.getElementById("registerPassword").value && document.getElementById("registerPassword").value.length >= 8) {
            if (document.getElementById("birthday").value) {
                document.getElementById("register").disabled = true
            } else
                document.getElementById("register").disabled = false
        } else
            document.getElementById("register").disabled = false
    }
    document.getElementById("register").disabled = false
}

window.createNewTopic = function () {
    let topic = {
        topic: localStorage.getItem("topicTopic"),
        description: document.getElementById("description").value,
        pictureName: localStorage.getItem("imgName")
    }
    let path = 'topics'
    postApi(path, topic)
        .then(() => viewTopic(true))
        .then(() => renderCommentsTable())
}

window.getTopic = async function (topic) {
    let path = 'topics/' + topic
    let response = await getApi(path)
    console.log(response)
    if (response.pictureName !== null) {
        localStorage.setItem("imgName", response.pictureName)
    } else {
        localStorage.removeItem("imgName")
    }
    localStorage.setItem("topicID", response.id)
    console.log(response.id)
    console.log(localStorage.getItem("topicID"))
    console.log(response.likes)
    document.getElementById("likeTopic").innerHTML = response.likes
    return response;
}

window.search = async function () {
    const searchWrapper = document.querySelector(".search-input");
    const inputBox = searchWrapper.querySelector("input");
    const suggBox = searchWrapper.querySelector(".autocom-box");

    let fetchList;
    inputBox.onkeyup = async (e) => {
        let userData = e.target.value;
        console.log(userData + " Userdata")
        fetchList = await fetchFilterTopic(userData);
        console.log(fetchList)
        if (fetchList !== null) {
            const topicList = fetchList.map((topic) => {
                console.log(topic.topic)
                console.log(topic.pictureName)
                return topic
            });

            if (userData.toLocaleLowerCase()) {
                let emptyArray = topicList.filter((topic) => {
                    return topic.topic.toLocaleLowerCase().startsWith(userData.toLocaleLowerCase());
                });

                emptyArray = emptyArray.map((topic) => {
                    let a = new Image()
                    let src = null;
                    if (topic.pictureName != null) {
                        console.log(topic.pictureName)
                        let typ = topic.pictureName.split(".")[1]
                        a.src = "data:image/" + typ + ";base64, " + topic.smallPicture;
                        console.log(topic.pictureName)
                        console.log(topic.smallPicture)
                        src = "data:image/" + typ + ";base64," + topic.smallPicture;

                    }
                    return topic = `<li><div id="topicShowText">${topic.topic}</div><div  id="topicShowImage"><img src=${src} /></div></li>`;
                });


                searchWrapper.classList.add("active");
                showTopics(emptyArray);

                let allList = suggBox.querySelectorAll("li");
                console.log(allList)
                for (let i = 0; i < allList.length; i++) {
                    allList[i].onclick = (v) => {
                        select(v.target);
                    }
                }
            } else {
                searchWrapper.classList.remove("active");

            }
        } else {
            searchWrapper.classList.remove("active");

            document.getElementById("showCreateTopic").hidden = true
            document.getElementById("showTopic").hidden = true
            document.getElementById("table").hidden = true

        }
    }
    window.select = function (element) {
        localStorage.removeItem("imgName")
        let selectData = element.textContent;
        inputBox.value = selectData;
        searchWrapper.classList.remove("active");
        localStorage.removeItem("topicTopic")
        console.log(fetchList)
        let tmp = fetchList.map((topic) => {
            if (selectData === topic.topic) {
                localStorage.setItem("topicTopic", topic.topic);
                console.log(selectData)
                return topic;
            } else {
                return null
            }
        });

        if (localStorage.getItem("topicTopic") == null) {
            console.log("jaa")
            tmp = null;
            console.log(selectData)
            selectData = selectData.substring(0, selectData.length - 11)
            console.log(selectData)
            inputBox.value = selectData
            localStorage.setItem("topicTopic", selectData);
        }
        viewTopic(tmp);
    }

    window.fetchFilterTopic = async (topic) => {
        console.log(topic)

        if (topic.trim() !== "") {
            let path = 'topics/filtered?topic=' + topic
            let response = await getApi(path)
            console.log(response)
            return response;
        }
        return null;
    }

    function showTopics(list) {
        let listData;
        let userValue;
        console.log(list)
        if (!list.length) {
            userValue = inputBox.value;
            listData = `<li><div>${userValue} (newTopic)</div><div></div></li>`;

        } else {
            listData = list.join('');
            userValue = inputBox.value;

            if (list.length < 7 && !list.toString().toLocaleUpperCase().includes(">" + userValue.toLocaleUpperCase() + "<")) {
                listData += `<li><div>${userValue} (newTopic)</div><div></div></li>`;
            }
        }
        suggBox.innerHTML = listData;
    }

    window.postPicture = async () => {
        let formData = new FormData();
        formData.append("imageFile", document.getElementById("imageFile").files[0]);
        let path = "pictures"
        let response = await postImg(path, formData)
        console.log(response.message);
        localStorage.setItem("imgName", response.message)
    }

    window.readURL = function (input) {
        if (input.files && input.files[0]) {
            let reader = new FileReader();
            reader.onload = function (e) {
                document.getElementById("blah").src = e.target.result;
            };
            reader.readAsDataURL(input.files[0]);
        }
    }

    window.loadPicture = async function () {
        console.log(localStorage.getItem("imgName"))
        let path = "pictures/?name=" + localStorage.getItem("imgName");
        let response = await getImg(path)
        const url = URL.createObjectURL(response)
        console.log(url)
        localStorage.setItem("imgUrl", url)
    }

    window.likeComment = async function (id) {
        let path = "like/comment/" + id
        let response = await postApi(path);
        if (response != null) {
            document.getElementById(id).value = response.message
            document.getElementById(id).classList.add("liked")
        }
    }

    window.likeTopic = async function () {
        let path = "like/topic/" + localStorage.getItem("topicID")
        let response = await postApi(path);
        if (response != null) {
            document.getElementById("likeTopic").innerText = response.message
            document.getElementById("likeTopic").classList.add("liked")
        }
    }
}
