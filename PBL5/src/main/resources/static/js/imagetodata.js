function convertImageToByteArray(imageFile) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = (event) => {
            const arrayBuffer = event.target.result;
            const uintArray = new Uint8Array(arrayBuffer);
            resolve(Array.from(uintArray));
        };
        reader.onerror = (event) => {
            reject(event.target.error);
        };
        reader.readAsArrayBuffer(imageFile);
    });
}

function saveImage(image){
    const imageData = convertImageToByteArray(image); // Đây là mảng byte hình ảnh đã được chuyển đổi
    const extraData = 15; // Giá trị dữ liệu bổ sung

    fetch('/image/receive', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/octet-stream', // Loại dữ liệu là mảng byte
            'X-Extra-Data': extraData // Thông tin bổ sung trong tiêu đề
        },
        body: imageData
    })
        .then((response) => {
            if (response.ok) {
                // Xử lý phản hồi thành công
                console.log('Yêu cầu đã được gửi thành công');
                closeModal("loadingModal");
                openModal("successModal");
            } else {
                // Xử lý phản hồi lỗi
                console.error('Lỗi khi gửi yêu cầu');
                closeModal("loadingModal");
                openModal("failedModal");
            }
        })
        .catch((error) => {
            // Xử lý lỗi trong quá trình gửi yêu cầu
            console.error('Lỗi khi gửi yêu cầu:', error);
            closeModal("loadingModal");
            openModal("successModal");
        });
}