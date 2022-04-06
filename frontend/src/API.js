export function handleResponse(response) {
    if (!response.ok || response.create) {
        response.json().then((responseBody) => {
            console.log(responseBody)
            return null;
        }).catch(() => {
            console.log("fehel")
            //fehler popup
            // alert()
        })

    } else {
        let a = response.json()
        console.log(a)
        return a
    }

}

export async function postLogin(path, body) {
    let response = await fetch(window.location.href + 'api/' + path, {
        method: 'POST',
        body: JSON.stringify(body),
        headers: {
            'Content-Type': 'application/json'
        }
    })
    return await handleResponse(response);
}

export async function postApi(path, body) {
    let response = await fetch(window.location.href + 'api/' + path, {
        method: 'POST',
        body: JSON.stringify(body),
        headers: {
            'Content-Type': 'application/json',
            "Authorization": 'Bearer ' + localStorage.getItem("jwt"),
        }
    })
    console.log(response)
    return await handleResponse(response);
}

export async function getApi(path) {
    console.log(path)
    const response = await fetch(window.location.href + 'api/' + path, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            "Authorization": 'Bearer ' + localStorage.getItem("jwt"),
        }
    })
    console.log(response)
    return await handleResponse(response);
}

export async function getImg(path) {
    const response = await fetch(window.location.href + 'api/' + path, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            "Authorization": 'Bearer ' + localStorage.getItem("jwt"),
        }
    })
    return await response.blob();
}

export async function postImg(path, body) {
    const response = await fetch(window.location.href + 'api/' + path, {
        method: 'POST',
        body: body,
        headers: {
            "Authorization": 'Bearer ' + localStorage.getItem("jwt"),
        }
    })
    return await response.json();
}